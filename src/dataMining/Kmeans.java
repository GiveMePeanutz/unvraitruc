// Kmeans.java 
 
package dataMining;
 
import java.awt.datatransfer.DataFlavor;
import java.util.Random;
import java.util.ArrayList;

import beans.KmeansDataType;
 
public class  Kmeans{
 
    //private double[][] data;         // data to cluster
    private KmeansDataType[] DATA;
    private int numClusters;    // number of clusters
    private double[][] clusterCenters;   // cluster centers
    private int dataSize;               // size of the data
    private int dataDim;                // dimension of the data
    //private ArrayList[] clusters;     // calculated clusters
    private ArrayList<KmeansDataType>[] clusteredDATA;
    private double[] clusterVars;        // cluster variances
 
    private double epsilon;
 
    public Kmeans(KmeansDataType[] DATA, int numClusters)
    {
        dataSize = DATA.length;
        dataDim = DATA[0].getData().length;
 
        this.DATA = DATA;
 
        this.numClusters = numClusters;
 
        this.clusterCenters =  new double[numClusters][dataDim-1];
 
        clusteredDATA = new ArrayList[numClusters];
        for(int i=0;i<numClusters;i++)
        {
            clusteredDATA[i] = new ArrayList();
        }
        clusterVars = new double[numClusters];
 
        epsilon = 0.0;
 
        randomizeCenters(numClusters, DATA);
    }
    
    
 
 

 
    private void randomizeCenters(int numClusters, KmeansDataType[] data) {
        Random r = new Random();
        int[] check = new int[numClusters];
        for (int i = 0; i < numClusters; i++) {
            int rand = r.nextInt(dataSize);
            if (check[i] == 0) {
            	this.clusterCenters[i] = data[rand].getData().clone();
                check[i] = 1;
            } else {
                i--;
            }
        }
    }
 
    private void calculateClusterCenters()
    {
        for(int i=0;i<numClusters;i++)
        {
            int clustSize = clusteredDATA[i].size();
 
            for(int k= 0; k < dataDim; k++)
            {
 
                double sum = 0d;
                for(int j =0; j < clustSize; j ++)
                {
                    double[] elem =  ((KmeansDataType)clusteredDATA[i].get(j)).getData();
                    sum += elem[k];
                }
 
                clusterCenters[i][k] = sum / clustSize;
            }
        }
    }
 
     private void calculateClusterVars()
    {
        for(int i=0;i<numClusters;i++)
        {
            int clustSize = clusteredDATA[i].size();
            Double sum = 0d;
 
                for(int j =0; j < clustSize; j ++)
                {
 
                    double[] elem = ((KmeansDataType)clusteredDATA[i].get(j)).getData();
 
                    for(int k= 0; k < dataDim; k++)
                    {
                        sum += Math.pow( (Double)elem[k] - getClusterCenters()[i][k], 2);
                    }
                }
            clusterVars[i] = sum / clustSize;
        }
    }
 
     public double getTotalVar()
    {
         double total = 0d;
         for(int i=0;i< numClusters;i++)
         {
             total += clusterVars[i];
         }
 
         return total;
     }
 
     public double[] getClusterVars()
    {
        return  clusterVars;
    }
 
     public ArrayList[] getClusters()
    {
         return clusteredDATA;
    }
 
     private void assignData()
    {
        for(int k=0;k<numClusters;k++)
        {
            clusteredDATA[k].clear();
        }
 
        for(int i=0; i<dataSize; i++)
        {
 
            int clust = 0;
            double dist = Double.MAX_VALUE;
            double newdist = 0;
 
            for(int j=0; j<numClusters; j++)
            {
                newdist = distToCenter( DATA[i].getData(), j );
                if( newdist <= dist )
                {
                    clust = j;
                    dist = newdist;
                }
            }
 
            clusteredDATA[clust].add(DATA[i]);
        }
 
    }
 
     private double distToCenter( double[] datum, int j )
    {
         double sum = 0d;
         for(int i=0;i < dataDim; i++)
         {
             sum += Math.pow(( datum[i] - getClusterCenters()[j][i] ), 2);
         }
 
         return Math.sqrt(sum);
    }
 
      public void calculateClusters()
    {
 
         double var1 = Double.MAX_VALUE;
         double var2;
         double delta;
 
        do
        {
              calculateClusterCenters();
              assignData();
              calculateClusterVars();
              var2 = getTotalVar();
              if (Double.isNaN(var2))    // if this happens, there must be some empty clusters
             {
                 delta = Double.MAX_VALUE;
                 randomizeCenters(numClusters, DATA);
                 assignData();
                 calculateClusterCenters();
                 calculateClusterVars();
             }
             else
             {
                 delta = Math.abs(var1 - var2);
                 var1 = var2;
             }
 
        }while(delta > epsilon);
    }
 
    public void setEpsilon(double epsilon)
    {
        if(epsilon > 0)
        {
            this.epsilon = epsilon;
        }
    }
 
    /**
     * @return the clusterCenters
     */
    public double[][] getClusterCenters() {
        return clusterCenters;
    }
    
    public void displayClusters(){
    	for(int k=0;k<numClusters;k++)
        {
    		
    		System.out.print(k+"  ----------");
    		for( int l=0; l<clusterCenters[0].length;l++){
    			System.out.print("  "+clusterCenters[k][l]);
    		}
    		System.out.println(" ");
    		for(int i = 0; i<clusteredDATA[k].size();i++){
    			System.out.print("  "+((KmeansDataType)clusteredDATA[k].get(i)).getUsername()+"  ");
    			double[] b = ((KmeansDataType)clusteredDATA[k].get(i)).getData();
    			for(int j=0; j<this.dataDim;j++){
    				System.out.print(b[j]+"   ");
    			}
    			System.out.println(" ");
    		}
    		
        }
    }
    
}