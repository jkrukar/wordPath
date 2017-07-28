//***********************************
//John Krukar
//I used this demo from the CS351 class website.
//***********************************

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class ReadTextFile
{
  private BufferedReader reader = null;

  public ReadTextFile(String path)
  {
    try
    {
      reader = new BufferedReader(new FileReader(path));
    }
    catch (IOException e)
    { String msg = "IO Exception: " + e.getMessage();
      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public String[] readWordsOnLine()
  {
    String str = null;
    try
    {
      str = reader.readLine();
    }
    catch (IOException e)
    { String msg = "readWordsOnLine(): IO Exception: " + e.getMessage();
      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
      e.printStackTrace();
    }


    if (str == null) return null;
    return  str.split(" ");
  }

  public void closeReader()
  {
    try
    {
      reader.close();
    }
    catch (IOException e)
    { String msg = "readWordsOnLine(): IO Exception: " + e.getMessage();
      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }
}
