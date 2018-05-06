package rot.simpletrees.model;

import java.util.ArrayDeque;
import java.util.Queue;

public class FibTree <K extends Comparable<K>, V>
	extends AVLTree<K, V> {

	public FibTree() { }

	// ability fibonacci tree
	private int ablFT()
	{
		/*
		return ( getFN(getHeight(m_root)+2)-1 == size() ) 
			? true
			: false;
		*/

		// is fibonacci number
		return isFibN(size()+1, 1);
	}
	private int isFibN(int num, int index) {
		int fibNum = getFN(index);

		if (num == fibNum ) return index;
		if (num < fibNum) return 0;
		return isFibN(num, ++index);
	}
	private int getFN(int index) // fibonacci number
	{ 
		// 1 1 2 3 5 ...
		// 1 2 3 4 5
		if(index <= 2) return 1;
		return getFN(index-1)+getFN(index-2);
	}

	public boolean resetToFib() {
		int index = ablFT();
		if(index == 0) return false;

		//save data
		Queue<Tree.Data<K, V>> data = new ArrayDeque<>(size());
		fillCollection(data);

		System.out.println("building");
		m_root = buildFib(m_root, index-2);

		System.out.println("filling");
		fillFib(m_root, data);

		return true;
	}
	protected BinTree.Node<K, V> buildFib(BinTree.Node<K, V> node, int h) {

		if( h == 0 ) return null;

		node = createNode(null);

		AVLTree.Node avlNode = (AVLTree.Node) node;
		avlNode.m_height = (byte)h;

		if( h > 1 ) {
			int itr = (Math.random() < 0.5) ? 1 : 0; //is tilt right
			node.m_left = buildFib(node.m_left, h-1-itr);
			node.m_right = buildFib(node.m_right, h-1-(1-itr));
		}
		
		return node;
	}
	protected void fillFib(BinTree.Node<K, V> node, Queue<Tree.Data<K, V>> data) {
		if(node == null) return;
		fillFib(node.m_left, data);	
		node.m_data = data.remove();
		fillFib(node.m_right, data);
	}	

	public boolean isFT()
	{
		return isFT(m_root, true);
	}
	protected boolean isFT(BinTree.Node<K, V> node, boolean result)
	{
		if(result == false) return result;
		if( getHeight(node) < 2 ) return true;

		result = isFT(node.m_left, result);
		if( Math.abs(getBalance(node)) != 1 ) result = false;
		result = isFT(node.m_right, result);

		return result;
	}
}
