import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class ID3_Algo {

	public static int ATTRB_MAX = 0;

	public ID3_Algo() {
		
	}

	public static ID3_Algo id3algo1 = new ID3_Algo();
	static ArrayList<DataRow> data_training = new ArrayList<DataRow>();
	static ArrayList<DataRow> data_test = new ArrayList<DataRow>();
	static ArrayList<Values_feature> attributes = new ArrayList<Values_feature>();
	

	public static void main(String[] args) {
		
		ArrayList<Values_feature> markedAttrb = new ArrayList<Values_feature>();
		if (args.length != 2) {
			System.out.println("You are only permitted to give two arguments: training data file and test data file");
			System.exit(1);

		}
		
		String recordsTraining = args[0];
		String recordsTest = args[1];

		boolean readFile = false;
		
		boolean isdataTrain = true;
		boolean isdataTest = false;
		readFile = id3algo1.dataRead(recordsTraining, data_training, isdataTrain);
		if (readFile == false) {
			System.out.println("There is some problem reading the file");
			System.exit(1);
		}

		
		readFile = id3algo1.dataRead(recordsTest, data_test, isdataTest);
		if (readFile == false) {
			System.out.println("There is some problem reading the file");
			System.exit(1);
		}
		
		Values_feature attrb;
		for (int i = 0; i < attributes.size(); i++) {
			attrb = attributes.get(i);
			
		}
		System.out.println();

		DataRow rec;
		
		Node root = id3algo1.new Node(data_training);
		Tree tree = id3algo1.new Tree();
		for (int i = 0; i < attributes.size(); i++) {
			String tmp = attributes.get(i).lable;
		
		}
		Values_feature duplicate = id3algo1.new Values_feature();
		duplicate.lable = "first";
		root.attrb = duplicate;
		root = tree.craeteTree(data_training, root, attributes, markedAttrb);
		
		tree.showTree(root, 0);
		double accuracyTraining = tree.findAccuracy(root, data_training);
		double accuracyTesting = tree.findAccuracy(root, data_test);
		System.out.println("\nAccuracy of training data is (" + data_training.size() +  ")" + accuracyTraining);
		System.out.println("\nAccuracy of testing data is (" + data_test.size() +  ")" + accuracyTesting);
	}

	class DataRow {

		public String rowtName = "";
		
		public String[] attributeValue = new String[ATTRB_MAX];

		public String finalClass;

                public DataRow() {

		}

	}

	class Values_feature {
		public String lable;
		public ArrayList<String> valuesFeature = new ArrayList<String>();
		public double entropy;
		public int MAX;

		public void setAttrb(ArrayList<DataRow> rows, int index) {
			DataRow tmp;
			for (int i = 0; i < rows.size(); i++) {
				tmp = rows.get(i);
				if (!valuesFeature.contains(tmp.attributeValue[index])) {
					valuesFeature.add(tmp.attributeValue[index]);
				}
			}

		}

		public int getAttrbIndex(String attrb_val) {
			String tmp;
			for (int i = 0; i < this.valuesFeature.size(); i++) {
				tmp = this.valuesFeature.get(i);
				if (tmp.equals(attrb_val)) {
					return i;
				}
			}
			
			return -1;
		}

	}

	public boolean dataRead(String fileName, ArrayList<DataRow> records,
			boolean isTrainData) {

		
		Scanner sc = null;
		try {
			sc = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			return false;
		}

		
		this.traverse(sc, records, isTrainData);
		return true;
	}

	public void traverse(Scanner sc, ArrayList<DataRow> records,boolean recTrain) {
		String ln = sc.nextLine().trim();
		int count = 0;
		int i =0;
		String token;
		StringTokenizer st = new StringTokenizer(ln);
		
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			Values_feature attribute = new Values_feature();
			if (recTrain) {
				attribute.lable = token;
			}
			int maximum = Integer.parseInt(st.nextToken());
			
			if (recTrain) {
				attribute.MAX = maximum;
				
				attributes.add(attribute);
				i++;
				
				count++;
			}
		}
		
		for ( i =0; i < attributes.size(); i++)
		{
			Values_feature attrb;
			attrb = attributes.get(i);
			
		}
		
		if (recTrain)
			ATTRB_MAX = count;

		
		while (sc.hasNextLine()) {
			ln = sc.nextLine().trim();
			st = new StringTokenizer(ln);
			 i = 0;
			String attrbTemp = null;
			DataRow set = id3algo1.new DataRow();
			count = 0;
			while (count < ATTRB_MAX) {
				attrbTemp = st.nextToken();
				
				set.attributeValue[count] = (attrbTemp);
				
				count++;
			}
			
			set.finalClass = st.nextToken();

			
			records.add(set);
		}
		
				for (i = 0; i < count; i++) {
					Values_feature attribute = attributes.get(i);
					attribute.setAttrb(ID3_Algo.data_training, i);
				}
				
				for ( i =0; i < count; i++)
				{
					Values_feature attribute = attributes.get(i);
					
					for ( int j =0; j < attribute.MAX; j++)
					{
						String tmp = attribute.valuesFeature.get(j);
						
					}
				}

	}

	public class Node {
		public Node parent;
		public Node[] child;
		public ArrayList<DataRow> rows;
		public ArrayList<Values_feature> markedAttrb;
		public double entropy;
		public int totalChild;
		public boolean isTravelrsed;
		Values_feature attrb;
		String valAttrb;

		public Node() {
			this.rows = new ArrayList<DataRow>();
			this.markedAttrb = new ArrayList<Values_feature>();
			entropy = 0.0;
			parent = null;
			child = null;
			isTravelrsed = false;
			attrb = null;
			valAttrb = null;
			totalChild = 0;
		}

		public Node(ArrayList<DataRow> records) {
			this.rows = new ArrayList<DataRow>();
			this.markedAttrb = new ArrayList<Values_feature>();
			this.rows = records;
			entropy = 0.0;
			parent = null;
			child = null;
			isTravelrsed = false;
			attrb = null;
			valAttrb = null;
			totalChild = 0;
		}
		
	}

	public class Tree {
		public Node craeteTree(ArrayList<DataRow> records, Node root, ArrayList<Values_feature> attributes, ArrayList<Values_feature> marked_attrb) {

			
			int bestAttrb = -1;
			double highestIG = 0;
			Entropy nextEntropy = new Entropy();
			root.entropy = nextEntropy.findEntropy(root.rows);

			if (root.entropy == 0) {
				return root;
			}
			
			for (int i = 0; i < ATTRB_MAX; i++) {
				Values_feature newAttrb = attributes.get(i);
				
				if (!isAttrbMarked(newAttrb, root.markedAttrb)) {
				
					double entropy = 0;
					ArrayList<Double> allentropies = new ArrayList<Double>();
					ArrayList<Integer> allsetSizes = new ArrayList<Integer>();

					for (int j = 0; j < newAttrb.MAX; j++) {
						ArrayList<DataRow> sub = sub(root, i,
								newAttrb.valuesFeature.get(j));
						allsetSizes.add(sub.size());

						if (sub.size() != 0) {
							entropy = nextEntropy.findEntropy(sub);
							allentropies.add(entropy);
						}
					}

					double IG = nextEntropy.findGain(root.entropy,
							allentropies, allsetSizes, root.rows.size());
					if (IG > highestIG) {
						bestAttrb = i;
						highestIG = IG;
					}
				}
			}
			if (bestAttrb != -1) {
				int sizeOfChild = attributes.get(bestAttrb).MAX;
				Values_feature tmp = attributes.get(bestAttrb);
				String attributeName = tmp.lable;
				
				root.child = new Node[sizeOfChild];
				
				root.totalChild = sizeOfChild;
				ArrayList<Values_feature> tmpMarkedAttrb= new ArrayList<Values_feature>();
				
				for (int i =0; i < root.markedAttrb.size(); i++)
				{
					Values_feature tempAttr = root.markedAttrb.get(i);
					tmpMarkedAttrb.add(tempAttr);
				}
				
				tmpMarkedAttrb.add(attributes.get(bestAttrb));
				
				for (int j = 0; j < sizeOfChild; j++) {
					root.child[j] = new Node();
					root.child[j].parent = root;
					root.child[j].rows = (sub(root, bestAttrb,
							tmp.valuesFeature.get(j)));
					root.child[j].attrb = tmp;
					root.child[j].valAttrb = tmp.valuesFeature.get(j);
					root.child[j].markedAttrb = tmpMarkedAttrb;
					
				}
				
				Node tmpNode;
				for (int j = 0; j < sizeOfChild; j++) {
				
					tmpNode = craeteTree(root.child[j].rows, root.child[j],
							attributes, tmpMarkedAttrb);
				}

			} else {
				
				return root;
			}

			return root;
		}

		public ArrayList<DataRow> sub(Node root, int attr, String value) {
			ArrayList<DataRow> subset1 = new ArrayList<DataRow>();

			for (int i = 0; i < root.rows.size(); i++) {
				DataRow rec = root.rows.get(i);

				if (rec.attributeValue[attr].equals(value)) {
					subset1.add(rec);
				}
			}
			String tmpName = attributes.get(attr).lable;
			
			return subset1;
		}

		public boolean isAttrbMarked(Values_feature attribute,
				ArrayList<Values_feature> markedAttrbLs) {
			if (markedAttrbLs.contains(attribute)) {
				return true;
			} else {
				return false;
			}
		}

		public void showTree(Node root, int flag) {
			if (root == null) {
				
				return;
			}
			
			if (!root.attrb.lable.equals("first"))
			{
			
			System.out.print("\n  ");
			for (int i = 0; i < flag; i++) {
				System.out.print("| ");
			}
			System.out.print(root.attrb.lable + " =" + root.valAttrb
					+ ":");
			flag++;
			}
			if (root.child == null) {
				String classlable = findLabel(root);
				System.out.print(classlable);
			}

			for (int i = 0; i < root.totalChild; i++) {
				showTree(root.child[i], flag);
			}

		}

		public int parseTree(Node root, ArrayList<DataRow> rows) {
			Queue<Node> que = new LinkedList<Node>();
 			int count = 0;
 			que.add(root);
			
			for (int i = 0; i < rows.size(); i++) {
				DataRow tmp = rows.get(i);
				Node tempNode = root;
				
				while (tempNode.child!= null ) {
					
					Values_feature attr = tempNode.child[0].attrb;
					int j = getAttrbIndex(attr);

					if (j != -1) {
						
						String attrValue = tmp.attributeValue[j];
						
						int k = attr.getAttrbIndex(attrValue);
						
						tempNode = tempNode.child[k];
					} else if (j == -1) {
						System.out.println("Something wrong while traversing the tree");
						return -1;
					}
				
				}
				if (tempNode.child == null) {
					String leaf = findLabel(tempNode);

					if (leaf.equals(tmp.finalClass)) {
						count++;
					}
				}
			}

			return count;
		}

		public double findAccuracy(Node root, ArrayList<DataRow> record) {
			double accuracy = 0.00;
			int count = 0;
			int size = record.size();

			count = parseTree(root, record);
			
			accuracy = ((double) count / (double) size) * 100;

			return accuracy;
		}

		public int getAttrbIndex(Values_feature attr) {
			Values_feature tmp;
			for (int i = 0; i < attributes.size(); i++) {
				tmp = attributes.get(i);
				if (tmp.lable.equals(attr.lable)) {
					return i;
				}
			}
			
			return -1;
		}

		public String findLabel(Node root) {
			ArrayList<DataRow> row = root.rows;
			DataRow tmp;
			int yCount = 0;
			int ncount = 0;
			
			for (int j = 0; j < row.size(); j++) {
				tmp = row.get(j);
				
				if (tmp.finalClass.equals("0")) {
					ncount++;
				}
				if (tmp.finalClass.equals("1")) {
					yCount++;
				}
			}

			if (yCount > ncount) {
				return "1";
			} else
				return "0";

		}
	}
	
	public class Entropy {
		public double findEntropy(ArrayList<DataRow> data) {
			double entropy = 0;

			if (data.size() == 0) {
				
				return 0;
			}

			for (int i = 0; i < 2; i++) {
				int count = 0;
				
				for (int j = 0; j < data.size(); j++) {
					DataRow rec = data.get(j);
					// If the output label matches 0 or 1 increase the count.
					if (rec.finalClass.equals(Integer.toString(i))) {
						count++;
					}
				}

				double p = count / (double) data.size();
				if (count > 0) {
					
					entropy += -p
							* (Math.log(p) / Math.log(2));
				}

			}

			return entropy;
		}

		public double findGain(double rootEntropy,
				ArrayList<Double> subEntropies, ArrayList<Integer> setSizes,
				int data) {
			double ig = rootEntropy;

			for (int i = 0; i < subEntropies.size(); i++) {
				ig += -((setSizes.get(i) / (double) data) * subEntropies
						.get(i));
			}

			return ig;
		}
	}

}
