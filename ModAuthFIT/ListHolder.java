package ModAuthFIT;


/**
* ModAuthFIT/ListHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from AuthFIT.idl
* Wednesday, November 30, 2016 7:10:06 PM GMT
*/

public final class ListHolder implements org.omg.CORBA.portable.Streamable
{
  public String value[] = null;

  public ListHolder ()
  {
  }

  public ListHolder (String[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = ModAuthFIT.ListHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    ModAuthFIT.ListHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return ModAuthFIT.ListHelper.type ();
  }

}
