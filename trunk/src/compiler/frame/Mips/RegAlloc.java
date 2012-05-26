package compiler.frame.Mips;

import java.util.Hashtable;

import compiler.temp.Temp;
import compiler.temp.TempList;
import compiler.temp.TempMap;
import compiler.assem.InstrList;
import compiler.flowgraph.AssemFlowGraph;
import compiler.flowgraph.FlowGraph;
import frame.Frame;
import compiler.graph.Node;
import compiler.graph.NodeList;

public class RegAlloc implements TempMap
{
	// Numero de registradores disponiveis
	private final static int NUM_REGS = 17;

	// Lista de registradores ( ou cores) disponiveis para alocacao
	private final static String[] regs =
		{
			"$t0",
			"$t1",
			"$t2",
			"$t3",
			"$t4",
			"$t5",
			"$t6",
			"$t7",
			"$t8",
			"$t9",
			"$s0",
			"$s1",
			"$s2",
			"$s3",
			"$s4",
			"$s5",
			"$s6"};

	// vetor que indica se um registrador esta livre
	private boolean[] freeReg = null;
	// Lista de instrucoes a serem analisadas
	private compiler.assem.InstrList intrs;
	//Grafo de fluxo associado a lista de instrucoes
	private FlowGraph flow;
	// frame associado ao alocador
	private Frame frame;
	// tabela de hash que ira mapear um temp com um registrador
	private Hashtable tempMap;

	private Temp[] precolored;

	public RegAlloc(Frame f, InstrList il, Temp[] precolored)
	{
		this.precolored = precolored;
		// Inicializa a lista de temporarios livres
		freeReg = new boolean[NUM_REGS];
		for (int i = 0; i < NUM_REGS; i++)
			freeReg[i] = true;
		// Define o frame associado ao alocador
		frame = f;
		tempMap = new Hashtable();
		// Constroe o grafo de fluxo do bloco de codigo
		flow = new AssemFlowGraph(il);
		setColors();
		//flow.show(System.out);
	}

	/* 
	 * Funcao de mapeamento. Verifica se o temporario
	 * passado por argumento nao e "precolorido", ou seja, ja
	 * nao e predefinido pelo frame. Se for, retorna o valor. Senao
	 * define uum valor.
	 */
	public String tempMap(Temp t)
	{
		Integer index = (Integer)tempMap.get(t);
		return regs[index.intValue()];

	}

	/*
	 * Define as cores do grafo de alocacao. ISto e, mapeia
	 * para cada temporario um registrador.
	 */
	private void setColors()
	{
		for (NodeList nl = flow.nodes(); nl != null; nl = nl.tail)
		{
			Node n = nl.head;
			TempList deflist = flow.def(n);

			// primeiro procura os temporarios definidos nos nos e aloca
			// um registrador livre. 
			if (deflist != null)
			{
				Temp t = deflist.head;
				if (!isPrecolored(t))
				{
					for (int i = 0; i < NUM_REGS; i++)
						if (freeReg[i])
						{
							freeReg[i] = false;
							tempMap.put(t, new Integer(i));
							break;
						}
				}
			}

			// Libera os registradores a medida que os temporarios alocados
			// sao utilizados como fonte. 
			for (TempList uselist = flow.use(n); uselist != null; uselist = uselist.tail)
			{
				Temp usetemp = uselist.head;
				if (!isPrecolored(usetemp))
				{
					Integer index = (Integer)tempMap.get(usetemp);
					if (index != null)
						freeReg[index.intValue()] = true;
				}
			}
		}
	}

	private boolean isPrecolored(Temp t)
	{
		for (int i = 0; i < precolored.length; i++)
			if (t.equals(precolored[i]))
				return true;
		return false;
	}
}
