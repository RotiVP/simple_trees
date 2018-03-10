package rot.simpletrees.example;

import rot.simpletrees.model.*;

import java.util.Scanner;
import java.util.Iterator;

public class CliTester {

	/*
	while( !sc.hasNext(ptrn) ) {
		sc.next();
		System.out.printf("%s\n", fail);
	}
	return sc.next();
	*/

	static public void main(String[] args) 
	{
		String welcome = 
			  "FibTree cli tester\n"
			+ "Type \'help\' to view a list of commands.";	
		String goodbye =
			"Bye.";
		String help =
			  " insert KEY"
			+ " print\n"
			+ " quit";
		String prompt =
			"(fibtree) ";

		FibTree<Integer, Integer> tree = new FibTree<>();
		Scanner sc = new Scanner(System.in);

		System.out.printf("%s\n", welcome);

		while(true) {
			System.out.printf("%s", prompt);

			String cmd = sc.nextLine().trim();
			if(cmd.isEmpty()) continue;

			if( cmd.equals("quit") ) break;
			else if( cmd.equals("help") ){
				System.out.printf("%s\n", help);

			}
			else if( cmd.matches("insert \\d{1,2}") ) {
				Integer key = Integer.parseInt(cmd.replaceAll("\\D+", ""));
				tree.insert(new FibTree.Data<Integer, Integer>(key, 0));
			}
			else if( cmd.equals("print") ) {
				FibTree.DataLevelsList<Integer, Integer> dls = tree.getDataLevels(); //data levels
				//int width = tree.getNodeCount();
				//dls.get(dls.size()-1).size();
				//System.out.print("printing "+dls.size());
				for(int i = 0; i < dls.size(); ++i) {
					//Iterator<FibTree.Data<Integer, Integer>> it = dls.get(i).iterator();
					//int span = Math.round(width/i);
					//width = Math.round((width-1)/2);
					//while(it.hasNext()) {
					for(int j = 0; j < dls.get(i).size(); ++j) {

						int curLevel = i; // current level (уровни нумеруются начиная с единицы)
						int levels = dls.size(); // levels
						int terms = (int) Math.pow(2, levels-1); // терминальных вершин
						int indent = (int)( terms/Math.pow(2, curLevel-1) ) - 1; // отступ с начала и с конца
						int gap = indent*2+1; // зазор между вершинами

						if(j == 0)
							for( int k = 0; k < indent; ++k )
								System.out.print("  ");
						else for( int k = 0; k < gap; ++k)
							System.out.print("  ");

						FibTree.Data data = dls.get(i).get(j);
						if(data != null)
							System.out.print(data.m_key);
					}
					System.out.println();
				}
			}
		}

		System.out.printf("%s\n", goodbye);
	}
}
