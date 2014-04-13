import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.text.DecimalFormat;


public class Main {
	
	public double[][] cost = null;
	public double[][] orginalCost = null;
	public int[][] mask = null;
	public int[] rowCover = null;
	public int[] colCover = null;
	public int step = 0;
	
	public int[][] path = null;
	public int path_count = 0;
	public int path_row_0 = 0;
	public int path_col_0 = 0;
	
	public int zero_row = 0;
	public int zero_col = 0;
	
	public int star_row = 0;
	public int star_col = 0;
	
	public int prime_row = 0;
	public int prime_col = 0;
	
	@SuppressWarnings({ "resource" })
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			BufferedReader br =new BufferedReader(new FileReader(args[0]));
			String line="";
			while((line=br.readLine())!=null){ 
				if(!line.isEmpty()){
					try{
						Main obj = new Main();
					    obj.orginalCost = obj.computeCost(line);
					    if(obj.orginalCost!=null){
					    	
					    	obj.cost = obj.subtractFromArr(obj.arrClone(obj.orginalCost), obj.arrMax(obj.orginalCost));
						    obj.mask = new int[obj.cost.length][obj.cost[0].length];
						    obj.rowCover = new int[obj.cost.length];
						    obj.colCover = new int[obj.cost[0].length];
						    obj.step = 1; 
						    obj.path = new int[obj.rowCover.length*obj.colCover.length][2];
						    obj.RunMunkres(obj);
						    DecimalFormat df = new DecimalFormat("##.00");
						    System.out.println(df.format(obj.sumMaskPoints(obj)));
					    }
					    else{
					    	System.out.println("0.00");
					    }
					}
					catch(Exception e){
						System.out.println(e.toString());
					}
				}
			}
			System.exit(0);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		

	}
	public double[][] computeCost(String line){
		String []arr = line.split(";");
		if(arr.length!=2){
			return null;
		}
		if(arr[0].isEmpty()){
			return null;
		}
		String []names = arr[0].split(",");
		String []products = arr[1].split(",");
		
		int dimension = (names.length >= products.length)?names.length:products.length;
		double [][]cost = new double[dimension][dimension];
		
		String name="",pname="";
		int vowelsN=0, consonantsN=0, totalN=0;
		int totalP=0;
		int gcd;
		
		for(int row=0; row<names.length; row++){
			name = names[row].replaceAll("[^a-zA-Z]", "");
			totalN = name.length();
			vowelsN = name.replaceAll("[^aeiouyAEIOUY]", "").length();
			consonantsN = totalN - vowelsN;
			
			for(int col=0; col<products.length;col++){
				pname = products[col].replaceAll("[^a-zA-Z]", "");
				totalP= pname.length();
				
				if(totalP%2==0)
					cost[row][col] = 1.5*vowelsN;
				else
					cost[row][col] = consonantsN;
				gcd = ((new BigInteger(String.valueOf(totalP))).gcd(new BigInteger(String.valueOf(totalN)))).intValue();
				
				if(gcd>1){
					cost[row][col] *= 1.5;
				}
			}
		}
		return cost;
	}
	public double rowMax(double [][]arr, int row){
		double rowMax = Double.MIN_VALUE;
		for(int col=0; col<arr[0].length; col++){
			rowMax = (arr[row][col] > rowMax)? arr[row][col]:rowMax;
		}
		return rowMax;
	}
	public double arrMax(double [][]arr){
		double max = Double.MIN_VALUE;
		double rowMaxi = 0;
		for(int row=0; row< arr.length; row++){
				rowMaxi = rowMax(arr,row);
				max = (rowMaxi>max)?rowMaxi:max;
		}
		return max;
	}
	public double[][] subtractFromArr(double [][]arr, double val){
		for(int row=0; row< arr.length; row++){
			for(int col=0; col<arr[0].length; col++){
				arr[row][col] = val -arr[row][col];
			}
		}
		return arr;
	}	
	
	public void RunMunkres(Main obj){
	
		Boolean done = false;
		
		while(!done){
			switch(obj.step){
				case 1:
					step1(obj);
					break;
				case 2:
					step2(obj);
					break;
				case 3:
					step3(obj);
					break;
				case 4:
					step4(obj);
					break;
				case 5:
					step5(obj);
					break;
				case 6:
					step6(obj);
					break;
				case 7:
					done = true;
					break;
			}
		}
	}

	
	public void step1(Main obj){
		obj.cost=subtractMinFromRespectiveRows(this.cost);
		obj.step = 2;
	}
	
	public static double[][] subtractMinFromRespectiveRows(double [][]arr){
		double min = 0.0;
		for(int row=0; row<arr.length; row++){
			min = rowMin(arr,row);
			arr = subtractFromRow(arr, row, min);
		}
		return arr;
	}
	public static double[][] subtractFromRow(double [][]arr, int row, double val){
		for(int col=0; col<arr[0].length; col++){
			arr[row][col]-=val;
		}
		return arr;
	}
	public static double rowMin(double [][]arr, int row){
		double rowMin = Double.MAX_VALUE;
		for(int col=0; col<arr[0].length; col++){
			rowMin = (arr[row][col] < rowMin)? arr[row][col]:rowMin;
		}
		return rowMin;
	}
	
	public void step2(Main obj){
		for(int row=0; row<obj.cost.length; row++){
			for(int col=0; col<obj.cost[0].length; col++){
				if(obj.cost[row][col]==0.0 && obj.rowCover[row]==0 && obj.colCover[col]==0){
					obj.mask[row][col] = 1;
					obj.rowCover[row] = 1;
					obj.colCover[col] = 1;
				}
			}
		}
		for(int row=0; row<obj.rowCover.length; row++){
			obj.rowCover[row] = 0;
		}
		for(int col=0; col<obj.colCover.length; col++){
			obj.colCover[col] = 0;
		}
		obj.step = 3;
	}
	
	public void step3(Main obj){
		int colCount;
		for(int row=0; row<obj.cost.length; row++){
			for(int col=0; col<obj.cost[0].length; col++){
				if(obj.mask[row][col]==1){
					obj.colCover[col] = 1;
				}
			}
		}
		colCount=0;
		for(int col=0; col<obj.colCover.length; col++){
			if(obj.colCover[col]==1){
				colCount += 1;
			}	
		}
		
		if(colCount >= obj.colCover.length || colCount >= obj.rowCover.length)
			obj.step = 7;
		else
			obj.step = 4;
	}

	public void step4(Main obj){
		
		Boolean done = false;
		while(!done){
			obj.find_a_zero(obj);
			if(obj.zero_row==-1){
				done=true;
				obj.step=6;
			}
			else{
				obj.mask[obj.zero_row][obj.zero_col] = 2;
				
				if(obj.star_in_row(obj.zero_row,obj)){
					obj.find_star_in_row(obj.zero_row, obj);
					obj.rowCover[obj.star_row] = 1;
					obj.colCover[obj.star_col] = 0;
				}
				else{
					done = true;
					obj.step = 5;
					obj.path_row_0 = obj.zero_row;
					obj.path_col_0 = obj.zero_col;
				}
			}
		}
	}
	
	public void find_a_zero(Main obj){
		Boolean done = false;
		obj.zero_row=-1;
		obj.zero_col=-1;
		for(int row=0; row<obj.rowCover.length && !done; row++){
			for(int col=0; col<obj.colCover.length; col++){
				if(obj.cost[row][col]==0.0 && obj.rowCover[row]==0 && obj.colCover[col]==0){
					obj.zero_row = row;
					obj.zero_col = col;
					done = true;
					break;
				}
			}
		}
	}
	public Boolean star_in_row(int rowInput, Main obj){
		for(int col=0; col<obj.colCover.length; col++){
			if(obj.mask[rowInput][col] == 1)
				return true;
		}
		return false;
	}
	public void find_star_in_row(int rowInput, Main obj){
		obj.star_col=-1;
		obj.star_row=rowInput;
		for(int col=0; col<obj.colCover.length; col++){
			if(obj.mask[rowInput][col] == 1)
				obj.star_col = col;
		}
	}
	
	public void step5(Main obj){
		Boolean done = false;
		
		obj.path_count = 1;
		obj.path[path_count-1][0] = obj.path_row_0;
		obj.path[path_count-1][1] = obj.path_col_0;
		
		while(!done){
			obj.find_star_in_col(obj.path[obj.path_count-1][1], obj);
			if(obj.star_row>-1){
				obj.path_count+=1;
				obj.path[obj.path_count-1][0] = obj.star_row;
				obj.path[obj.path_count-1][1] = obj.path[obj.path_count-2][1];
			}
			else{
				done = true;
			}
			if(!done){
				obj.find_prime_in_row(obj.path[obj.path_count-1][0], obj);
				obj.path_count+=1;
				obj.path[obj.path_count-1][0] = obj.path[obj.path_count-2][0];
				obj.path[obj.path_count-1][1] = obj.prime_col;
			}
		}
		obj.augment_path(obj);
		obj.clear_covers(obj);
		obj.erase_primes(obj);
		obj.step = 3;
	}
	
	public void find_star_in_col(int colInput, Main obj){
		obj.star_row = -1;
		obj.star_col = colInput;
		for(int row=0; row<obj.rowCover.length; row++){
			if(obj.mask[row][colInput] == 1)
				obj.star_row = row;
		}
	}
	public void find_prime_in_row(int rowInput, Main obj){
		obj.prime_row = rowInput;
		obj.prime_col = -1;
		for(int col=0; col<obj.colCover.length; col++){
			if(obj.mask[rowInput][col]==2)
				obj.prime_col = col;
		}
	}
	public void augment_path(Main obj){
		for(int i=0; i<obj.path_count; i++){
			if(obj.mask[ obj.path[i][0] ][ obj.path[i][1] ] == 1)
				obj.mask[ obj.path[i][0] ][ obj.path[i][1] ] = 0;
			else
				obj.mask[ obj.path[i][0] ][ obj.path[i][1] ] = 1;
		}
	}
	public void clear_covers(Main obj){
		for(int row=0; row<obj.rowCover.length; row++)
			obj.rowCover[row] = 0;
		for(int col=0; col<obj.colCover.length; col++)
			obj.colCover[col] = 0;
	}
	public void erase_primes(Main obj){
		for(int row=0; row<obj.rowCover.length; row++){
			for(int col=0; col<obj.colCover.length; col++){
				if(obj.mask[row][col]==2)
					obj.mask[row][col] = 0;
			}
		}
	}
	
	public void step6(Main obj){
		double minVal = find_smallest(obj);
		for(int row=0; row<obj.rowCover.length; row++){
			for(int col=0; col<obj.colCover.length; col++){
				if(obj.rowCover[row]==1)
					obj.cost[row][col]+=minVal;
				if(obj.colCover[col]==0)
					obj.cost[row][col]-=minVal;
			}
		}
		obj.step = 4;
	}
	public double find_smallest(Main obj){
		double minVal = Double.MAX_VALUE;
		for(int row=0; row<obj.rowCover.length; row++){
			for(int col=0; col<obj.colCover.length; col++){
				if(obj.rowCover[row]==0 && obj.colCover[col]==0){
					if(minVal > obj.cost[row][col])
						minVal = obj.cost[row][col];
				}
			}
		}
		return minVal;
	}
	
	public double[][] arrClone(double [][]arr){
		double [][]arrNew = new double[arr.length][arr[0].length];
		for(int row=0; row<arr.length; row++){
			for(int col=0; col< arr[0].length; col++){
				arrNew[row][col] = arr[row][col];
			}
		}
		return arrNew;
	}
	public double sumMaskPoints(Main obj){
		double sum = 0.0;
		for(int row=0; row<obj.rowCover.length; row++){
			for(int col=0; col<obj.colCover.length; col++){
				if(obj.mask[row][col]==1)
					sum+=obj.orginalCost[row][col];
			}
		}
		return sum;
	}
	
	public void printArr(double[][] arr){
		for(int i=0; i<arr.length; i++){
			for(int j=0; j<arr[0].length; j++){
				System.out.print(arr[i][j]+"\t");
			}
			System.out.println("");
		}
	}
	public void printArr(int[][] arr){
		for(int i=0; i<arr.length; i++){
			for(int j=0; j<arr[0].length; j++){
				System.out.print(arr[i][j]+"\t");
			}
			System.out.println("");
		}
	}
}
