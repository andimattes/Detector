
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;


public class Math_Detector implements PlugInFilter 
{
    public int setup(String arg, ImagePlus imp) 
    {
    	return DOES_8G;
    }
        
    public void run(ImageProcessor original) 
    {
    	ImageProcessor copy = original.duplicate();
    	int w = original.getWidth();
        int h = original.getHeight();
        
        //Arrays
        int[] storex = new int [10000];
        int[] storey = new int [10000];
        int storepos = 0;
        
        //Variablen
        int x = 0, y = 0, objekte = 0;
        int schwarz = 0, grau = 70;  //Farben definieren
        
        //Nullsetzen der Arrays
        for(int initx=0; initx<w; initx++)
        {
        	storex[initx]=0;
        }
        for(int inity=0; inity<h; inity++)
        {
        	storey[inity]=0;
        }
        
		for (y = 0; y < h; y++)
		{			
			for (x = 0; x < w; x++)
			{
				if (original.getPixel(x, y)==schwarz)
				{
					objekte++;
					System.out.printf("Position des ersten gefundenen Pixels des %d. Objekts: (%d|%d)\n", objekte, x, y);
					original.putPixel(x, y, grau);
					int xx = x;
					int yy = y;
					search(original, xx, yy, schwarz, grau, storepos, storex, storey );
					/*while (storepos > 0); //<--Wie k�nnen f�r storepos negative Werte vorkommen???
					{
						xx = storex[storepos];
						yy = storey[storepos];
						storepos--;
					}*/
				}
			}
		}
    	System.out.printf("Ende des U-Programms. Letzes Pixel erreicht\n");
    	System.out.printf("Es wurden %d Objekte gefunden\n", objekte);


    }//END
    
    //UP
    public void search (ImageProcessor original, int xx, int yy, int schwarz, int grau, int storepos, int storex[], int storey[] )
    {
    	System.out.printf("Hochz�hlen 9x\n");
    	for (int findy = yy-1; findy <= yy+1; findy++)
		{
			for (int findx = xx-1; findx <= xx+1; findx++)
			{
				if (original.getPixel(findx, findy)==schwarz)
				{
					//speichern
					storepos++;
					storex[storepos] = findx;
					storey[storepos] = findy;
					
					original.putPixel(findx, findy, grau);		//grau setzen
					System.out.printf("Neuer Pixel auf %d. Position im Array gespeichert!\n" ,storepos);
				}
			}
		}
		while (storepos != 0); //<--Wie k�nnen f�r storepos negative Werte vorkommen???
		{
			xx = storex[storepos];
			yy = storey[storepos];
			storepos--;
			System.out.printf("Pixel wurde abgearbeitet. Neue Arrayposition ist %d\n" ,storepos);
			search(original, xx, yy, schwarz, grau, storepos, storex, storey );	
		}
    }
}	//END








//UEBUNGEN:
/*


	//Filterprogramm mit einstellbaren Filter

    public void run(ImageProcessor original) 
    {
    	//ImagePlus img = IJ.openImage("C:/blume.tif"); //Wie automatisch Datei einlesen???!
        //ImageProcessor original = img.getProcessor(); 
        //byte[] bild = (byte[])original.getPixels();
    	
    	
    	// show text in status bar
  		IJ.showStatus("Punktez�hler von Stefan Erben");
  		// set value of progress bar
  		IJ.showProgress(0.0);
  		// display error message
  		IJ.error("Welchen Filter wollen Sie anwenden? (3x3, 5x5) ?");
  		IJ.showProgress(0.5);
   		// display text input dialog
   		int var = 0;
   		var = (int) IJ.getNumber("Ungerade Zahl eingeben", var);
    	
    	int w = original.getWidth();
        int h = original.getHeight();
        ImageProcessor copy = original.duplicate();
          
		for (int x = 0; x <= w; x++) 
		{
						
			for (int y = 0; y <= h; y++) 
			{
				int sum = 0;

				int i, j, filter;
				filter = (var - 1) / 2;
				for (j=-filter; j<=filter; j++)
				{
					for (i=-filter; i<=filter; i++)
					{
						int p = copy.getPixel(x+j, y+i);
						sum = sum + p;
					}
				}
				int q = sum / (var*var);
				original.putPixel(x, y, q);
	         }
         }
		IJ.showProgress(0.9);
		IJ.showMessage("Der" ,var+ "er Filter wird jetzt angewendet!");
		IJ.showProgress(1.0);
              
      }



// Ein- und Ausgabe Fenster, Zahlt Punkte und kreist sie grau ein, Prozessladebalken
public void run(ImageProcessor original) 
    {
    	// show text in status bar
  		IJ.showStatus("Punktez�hler von Stefan Erben");
  		// set value of progress bar
  		IJ.showProgress(0.0);
  		// display error message
  		IJ.error("Wollen Sie wirklich alle Punkte z�hlen?");
  		IJ.showProgress(0.5);
  		IJ.error("Sind Sie sich wirklich sicher?");
   		// display text input dialog
   		String name = IJ.getString("Geben Sie Ihren Namen ein: ","Name");
   		
    	
    	int w = original.getWidth();
        int h = original.getHeight();
        int r = w / 33; 					//Rahmenbreite
        int zaehler = 0;
        ImageProcessor copy = original.duplicate();
          
		for (int x = 0; x <= w; x++) 
		{
						
			for (int y = 0; y <= h; y++) 
			{
				int p = copy.getPixel(x, y);
				
				if (x <= r || x >= w-r || y <= r || y >= h-r)		//Macht einen Rahmen, Breite = 1/20 der Bildbreite
				{
					original.putPixel(x, y, 0);
				}
				else if (p < 10)
				{
					original.setValue (150);
					original.drawOval((x-5), (y-5), 10, 10);
					zaehler++;
				}
				
				//int p = original.getPixel(x, y);
				//original.putPixel((x-h), y, p);
				
				
             }
         }
		IJ.showProgress(0.9);
		IJ.showMessage(name+ ", das Ergebnis lautet wie folgt:");
		IJ.showMessage("Ingesamt wurden" ,zaehler+ " Punkte gefunden!");
		IJ.showProgress(1.0);
		
                
      }
      
      







// INVERTER
public void run(ImageProcessor original) 
{
    int w = original.getWidth();
    int h = original.getHeight();
    
    
	for (int x = 0; x <= w; x++) 
	{
		for (int y = 0; y <= h; y++) 
		{
			int p = original.getPixel(x,y);
			original.putPixel(x, y, (255-p));
          
         }
     }
            
  }


//Binaerwandler

public void run(ImageProcessor original) 
{
    int w = original.getWidth();
    int h = original.getHeight();
    
    
	for (int x = 0; x <= w; x++) 
	{
		for (int y = 0; y <= h; y++) 
		{
			int p = original.getPixel(x,y);
			if (p <105)				//weis oder schwarz		
			{
				original.putPixel(x, y, 255);
			}
			else
			{
				original.putPixel(x, y, 0);

			}
          
         }
     }
            
 }
  
  
  
// einzelne schwarze Pixel mit grauem Kreis einkreisen
 	
 	public void run(ImageProcessor original) 
    {
        int w = original.getWidth();
        int h = original.getHeight();
          
		for (int x = 0; x <= w; x++) 
		{
			for (int y = 0; y <= h; y++) 
			{
				int p = original.getPixel(x, y);
				if (p < 10)
				{
					original.setValue (150);
					original.drawOval((x-5), (y-5), 10, 10);
				}
				
				//int p = original.getPixel(x, y);
				//original.putPixel((x-h), y, p);
				
				
             }
         }
                
      }

*/