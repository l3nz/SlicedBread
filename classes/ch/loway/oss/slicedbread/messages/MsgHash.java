
package ch.loway.oss.slicedbread.messages;

import ch.loway.oss.slicedbread.containers.PID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An immutable message.
 * The message is basically a map that has an enum as its type, 
 * so that you can use a switch statement over it.
 * Its keys can be Enums or Strings, and you can get a value as 
 * an Enum, a String or an integer. 
 * It tries its best to convert them (so e.g. you can read an Enum 
 * value as a String with the same type) but in general you should 
 * read them with the native type.
 * 
 * It uses an inner Builder that uses a flow-style GUI; you get the 
 * message calling the seal() method, after which it is immutable.
 * 
 * @author lenz
 * @since  Jan 17, 2015
 */
public class MsgHash extends MsgCustom {

    private final Enum type ;
    private final Map  map  = new HashMap();

    private MsgHash( Enum e) {
        this.type = e;
    }
    
    /**
     * Returns the type of the object, so you can switch over it.
     * 
     * @return the type.
     */
    public Enum t() {
        return type;
    }
    
    /**
     * Reads a key as a String.
     * 
     * The key is looked up as its correct type, so e.g. a key 
     * of type Enum and a key of type String can co-exist.
     * 
     * @param e the key.
     * @return the value, converted as a String. If empty returns "".
     */
    
    public String getS( Enum e ) {
        return convString( map.get(e) );
    }
    
    
    /**
     * Reads a key as an integer.
     * 
     * The key is looked up as its correct type, so e.g. a key 
     * of type Enum and a key of type String can co-exist.
     * 
     * @param e the key.
     * @return the value, converted as an integer. If empty returns 0.
     */
    
    
    public int getI( Enum e ) {
        return convInt( map.get(e) ); 
    }
    
    
    /**
     * Reads a key as an Enum.
     * 
     * The key is looked up as its correct type, so e.g. a key 
     * of type Enum and a key of type String can co-exist.
     * 
     * @param e the key.
     * @return the value, or null.
     */
    
    public Enum getE( Enum e ) {
        return convEnum( map.get(e) );
    }
    
    
    /**
     * Reads a key as a String.
     * 
     * See the same method having Enum as a lookup key.
     * 
     * @param e the key.
     * @return the value
     */
    
    public String getS( String e ) {
        return convString( map.get(e) );
    }
    
    
    /**
     * Reads a key as an Integer.
     * 
     * See the same method having Enum as a lookup key.
     * 
     * @param e the key.
     * @return the value.
     */
    
    public int getI( String e ) {
        return convInt( map.get(e) );
    }
    
    
    /**
     * Reads a key as an Enum.
     * 
     * See the same method having Enum as a lookup key.
     * 
     * @param e the key.
     * @return the value.
     */
    
    public Enum getE( String e ) {
        return convEnum( map.get(e) );
    }
    
    
    
    
    
    private Enum convEnum( Object o ) {
        if ( o instanceof Enum ) {
            return (Enum) o;
        } else {
            return  null;
        }
    }
    
    private int convInt( Object o ) {
        if ( o instanceof Integer ) {
            return ((Integer) o).intValue();
        } else
        if ( o instanceof String) {
            
            try {
                return Integer.parseInt( (String) o );
            } catch ( NumberFormatException e ) {
                return 0;
            }
            
        } else {
            return 0;
        }
    }
    
    private String convString( Object o ) {
        if ( o instanceof String ) {
            return (String) o;
        } else 
        if ( o instanceof Enum ) {
            return ((Enum) o).toString();
        } else
        {
            return "";
        }
    }

    
    
    /**
     * Pretty-prints the object.
     * 
     * E.g. 
     * M:LOCC from#1=>to#2 'Hello':there MIZZY:Miz 
     * 
     * @return the printout.
     */
    
    @Override
    public String toString() {
    
        StringBuilder sb = new StringBuilder();
        
        sb.append( "M:")
          .append( type )
          .append( " " )
          .append( this.getFromPid() )
          .append( "=>" )
          .append( this.getToPid() )
          .append( " ");
        
        List<String> lS = new ArrayList<String>();
        List<Enum>   lE = new ArrayList<Enum>();
        
        for ( Object o: map.keySet() ) {
            if ( o instanceof String ) {
                lS.add( (String) o );
            } else
            if ( o instanceof Enum ) {
                lE.add( (Enum) o );
            }
        }
        
        Collections.sort(lE);
        Collections.sort(lS);
        
        for ( String s: lS) {
            sb.append( "'" ).append( s ).append( "':" )
              .append( map.get(s) )
              .append( " ");
        }

        for ( Enum e: lE) {
            sb.append( "" ).append( e ).append( ":" )
              .append( map.get(e) )
              .append( " ");
        }

        sb.append( "\n" );
        return sb.toString();
    }

    
    /***
     * The Builder.
     */
    
    public static class Builder {

        MsgHash m = null;

        /**
         * A type is required so you can switch over it.
         * 
         * @param type 
         */
        
        public Builder(Enum type) {
            m = new MsgHash(type);
        }
        
        /**
         * A static, flow-style constructor.
         * 
         * @param type
         * @return 
         */
        
        public static Builder build( Enum type ) {
            return new Builder( type );
        }
        

        public Builder put(Enum e, String s) {
            m.map.put(e, s);
            return this;
        }

        public Builder put(Enum e, Integer i) {
            m.map.put(e, i);
            return this;
        }

        public Builder put(Enum e, Enum s) {
            m.map.put(e, s);
            return this;
        }

        public Builder put(String e, String s) {
            m.map.put(e, s);
            return this;
        }

        public Builder put(String e, Integer i) {
            m.map.put(e, i);
            return this;
        }

        public Builder put(String e, Enum s) {
            m.map.put(e, s);
            return this;
        }

        public Builder setRecipients(PID from, PID to) {
            m.setRecipients(from, to);
            return this;
        }

        /**
         * Genrrates the messega, now immutable.
         * 
         * @return THe message. 
         */
        public MsgHash seal() {
            return m;
        }

    }
    
    
}

