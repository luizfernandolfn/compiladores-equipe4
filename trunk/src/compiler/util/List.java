package compiler.util;

public class List<E>
{
        public E head;
        public List<E> tail;
        
        public List(E h, List<E> t)
        {
        //if ( h == null )
        //    throw new Error();
        
                head = h;
                tail = t;
        }
    
    public int size()
    {
        if ( tail == null )
            return 1;
        
        return 1 + tail.size();
    }
    
    /* remove some guy from the list */
    public void remove(int index)
    {   
        int pos = 0;
        
        for (List<E> t = this; t != null; t = t.tail)
        {
                if (index == 0)
                {
                        if (t.tail != null)
                                t.head = t.tail.head;
                        else
                                t.head = null;
                        return;
                }
                else if (pos == index-1)
                {
                        t.tail = t.tail.tail;
                }       
        }
    }
}