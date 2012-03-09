/* Generated By:JavaCC: Do not edit this line. MiniJavaParserTokenManager.java */
package compiler.javacc;

/** Token Manager. */
public class MiniJavaParserTokenManager implements MiniJavaParserConstants
{

  /** Debug output. */
  public static  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public static  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private static final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0xf7eL) != 0L)
         {
            jjmatchedKind = 14;
            return 1;
         }
         return -1;
      case 1:
         if ((active0 & 0x800L) != 0L)
            return 1;
         if ((active0 & 0x77eL) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 1;
            return 1;
         }
         return -1;
      case 2:
         if ((active0 & 0x100L) != 0L)
            return 1;
         if ((active0 & 0x67eL) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 2;
            return 1;
         }
         return -1;
      case 3:
         if ((active0 & 0x260L) != 0L)
            return 1;
         if ((active0 & 0x41eL) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 3;
            return 1;
         }
         return -1;
      case 4:
         if ((active0 & 0x406L) != 0L)
            return 1;
         if ((active0 & 0x18L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 4;
            return 1;
         }
         return -1;
      default :
         return -1;
   }
}
private static final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
static private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
static private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 40:
         return jjStopAtPos(0, 23);
      case 41:
         return jjStopAtPos(0, 26);
      case 59:
         return jjStopAtPos(0, 28);
      case 83:
         return jjMoveStringLiteralDfa1_0(0x80L);
      case 91:
         return jjStopAtPos(0, 24);
      case 93:
         return jjStopAtPos(0, 25);
      case 99:
         return jjMoveStringLiteralDfa1_0(0x2L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x400L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x900L);
      case 109:
         return jjMoveStringLiteralDfa1_0(0x40L);
      case 112:
         return jjMoveStringLiteralDfa1_0(0x8L);
      case 115:
         return jjMoveStringLiteralDfa1_0(0x10L);
      case 116:
         return jjMoveStringLiteralDfa1_0(0x200L);
      case 118:
         return jjMoveStringLiteralDfa1_0(0x20L);
      case 119:
         return jjMoveStringLiteralDfa1_0(0x4L);
      case 123:
         return jjStopAtPos(0, 22);
      case 125:
         return jjStopAtPos(0, 27);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
static private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x440L);
      case 102:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(1, 11, 1);
         break;
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x4L);
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x2L);
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x100L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x20L);
      case 114:
         return jjMoveStringLiteralDfa2_0(active0, 0x200L);
      case 116:
         return jjMoveStringLiteralDfa2_0(active0, 0x90L);
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x8L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
static private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa3_0(active0, 0x12L);
      case 98:
         return jjMoveStringLiteralDfa3_0(active0, 0x8L);
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0x64L);
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x400L);
      case 114:
         return jjMoveStringLiteralDfa3_0(active0, 0x80L);
      case 116:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(2, 8, 1);
         break;
      case 117:
         return jjMoveStringLiteralDfa3_0(active0, 0x200L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
static private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 100:
         if ((active0 & 0x20L) != 0L)
            return jjStartNfaWithStates_0(3, 5, 1);
         break;
      case 101:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(3, 9, 1);
         break;
      case 105:
         return jjMoveStringLiteralDfa4_0(active0, 0x80L);
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0xcL);
      case 110:
         if ((active0 & 0x40L) != 0L)
            return jjStartNfaWithStates_0(3, 6, 1);
         break;
      case 115:
         return jjMoveStringLiteralDfa4_0(active0, 0x402L);
      case 116:
         return jjMoveStringLiteralDfa4_0(active0, 0x10L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
static private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x4L) != 0L)
            return jjStartNfaWithStates_0(4, 2, 1);
         else if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(4, 10, 1);
         break;
      case 105:
         return jjMoveStringLiteralDfa5_0(active0, 0x18L);
      case 110:
         return jjMoveStringLiteralDfa5_0(active0, 0x80L);
      case 115:
         if ((active0 & 0x2L) != 0L)
            return jjStartNfaWithStates_0(4, 1, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
static private int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 99:
         if ((active0 & 0x8L) != 0L)
            return jjStartNfaWithStates_0(5, 3, 1);
         else if ((active0 & 0x10L) != 0L)
            return jjStartNfaWithStates_0(5, 4, 1);
         break;
      case 103:
         if ((active0 & 0x80L) != 0L)
            return jjStopAtPos(5, 7);
         break;
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
static private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 17;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 15)
                        kind = 15;
                     jjCheckNAddStates(0, 5);
                  }
                  else if (curChar == 45)
                     jjstateSet[jjnewStateCnt++] = 4;
                  else if (curChar == 46)
                     jjCheckNAdd(3);
                  break;
               case 1:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 2:
                  if (curChar == 46)
                     jjCheckNAdd(3);
                  break;
               case 3:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjCheckNAdd(3);
                  break;
               case 4:
                  if (curChar == 45)
                     jjAddStates(6, 8);
                  break;
               case 6:
                  if ((0x2400L & l) != 0L && kind > 18)
                     kind = 18;
                  break;
               case 7:
                  if (curChar == 10 && kind > 18)
                     kind = 18;
                  break;
               case 8:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 9:
                  if (curChar == 45)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 10:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 15)
                     kind = 15;
                  jjCheckNAddStates(0, 5);
                  break;
               case 11:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 15)
                     kind = 15;
                  jjCheckNAdd(11);
                  break;
               case 12:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(12, 13);
                  break;
               case 13:
                  if (curChar != 46)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjCheckNAdd(14);
                  break;
               case 14:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjCheckNAdd(14);
                  break;
               case 15:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(15, 2);
                  break;
               case 16:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 17)
                     kind = 17;
                  jjCheckNAdd(16);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 1:
                  if ((0x7fffffe00000000L & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjCheckNAdd(1);
                  break;
               case 5:
                  if ((0x7fffffe00000000L & l) != 0L)
                     jjAddStates(6, 8);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 17 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   11, 12, 13, 15, 2, 16, 5, 6, 8, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", "\143\154\141\163\163", "\167\150\151\154\145", 
"\160\165\142\154\151\143", "\163\164\141\164\151\143", "\166\157\151\144", "\155\141\151\156", 
"\123\164\162\151\156\147", "\151\156\164", "\164\162\165\145", "\146\141\154\163\145", "\151\146", null, 
null, null, null, null, null, null, null, null, null, "\173", "\50", "\133", "\135", 
"\51", "\175", "\73", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0x1fc3cfffL, 
};
static final long[] jjtoSkip = {
   0x3c0000L, 
};
static protected JavaCharStream input_stream;
static private final int[] jjrounds = new int[17];
static private final int[] jjstateSet = new int[34];
static protected char curChar;
/** Constructor. */
public MiniJavaParserTokenManager(JavaCharStream stream){
   if (input_stream != null)
      throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", TokenMgrError.STATIC_LEXER_ERROR);
   input_stream = stream;
}

/** Constructor. */
public MiniJavaParserTokenManager(JavaCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
static public void ReInit(JavaCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
static private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 17; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
static public void ReInit(JavaCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
static public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

static protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

static int curLexState = 0;
static int defaultLexState = 0;
static int jjnewStateCnt;
static int jjround;
static int jjmatchedPos;
static int jjmatchedKind;

/** Get the next Token. */
public static Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100000600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

static private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
static private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
static private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

static private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
