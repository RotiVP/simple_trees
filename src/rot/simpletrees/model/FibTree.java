package rot.simpletrees.model;

import java.util.ArrayList;

public class FibTree <K extends Comparable<K>, V> 
	extends BinTree <K, V> {
	
	static class Node <K extends Comparable<K>, V> 
			extends BinTree.Node<K, V> {
		Node(	Data<K, V>	data,
				Node<K, V>	left,
				Node<K, V>	right) {
			super(data, null, null);
			m_left = left;
			m_right = right;
		}

		Node<K, V> m_left;
		Node<K, V> m_right;

		byte m_height = 0;
	}
	Node<K, V> m_root = null;

	public static class DataLevelsList<K extends Comparable<K>, V>
		extends ArrayList<ArrayList<Data<K, V>>> {
		public DataLevelsList(int initCap) 
		{
			super(initCap);
		}
   	}

	// --- constructors
	
	/*
	public FibTree( Data<K, V>... init )
	{
		for( Data<K, V> e : init )
			insert(e);	
	}
	*/
	public FibTree() {}

	// --- end ctors

	// --- AVL

	byte getHeight(Node<K, V> node)
	{
		return (node != null) ? node.m_height : 0;
	}
	byte getBalance(Node<K, V> root)
	{
		return (byte)(getHeight(root.m_left) - getHeight(root.m_right));
	}
	void fixHeight(Node<K, V> node)
	{
		int lh = getHeight(node.m_left);
		int rh = getHeight(node.m_right);
		node.m_height = (byte)( ((lh > rh) ? lh : rh)+1 );
	}
	Node<K, V> balance(Node<K, V> root)
	{
		fixHeight(root);

		if(getBalance(root) == 2) {
			if(getBalance(root.m_right) < 0 /*== -1*/) {
				root.m_right = rotateR(root.m_right);
			}
			root = rotateL(root);
		}
		else if (getBalance(root) == -2) {
			if(getBalance(root.m_left) > 0 /*== 1*/) {
				root.m_left = rotateL(root.m_left);
			}
			root = rotateR(root);
		}

		return root;
	}
	Node<K, V> rotateR(Node<K, V> root)
	{
		Node<K, V> newRoot = root.m_left;
		root.m_left = newRoot.m_right;
		newRoot.m_right = root;

		fixHeight(root);
		fixHeight(newRoot);
		return newRoot;
	}
	Node<K, V> rotateL(Node<K, V> root)
	{
		Node<K, V> newRoot = root.m_right;
		root.m_right = newRoot.m_left;
		newRoot.m_left = root;

		fixHeight(root);
		fixHeight(newRoot);
		return newRoot;
	}

	public DataLevelsList<K, V> getDataLevels()
	{
		DataLevelsList<K, V> dls = new DataLevelsList<>(getHeight(m_root)+1);
		fillDataLevels((Node<K,V>)m_root, dls, 0);
		return dls;
	}
	void fillDataLevels(
			Node<K, V> node,
			DataLevelsList<K, V> dls, 
			int level)
	{
		System.out.print("HERE");
		/*
		if (node == null) { 
			return;
				
		}
		*/
		System.out.print("hmm");
		byte nh = getHeight(node);//node height
		if(nh!=0) 
			fillDataLevels(node.m_left, dls, level+1);
		dls.get(level).add(node.m_data);
		if(nh!=0)
			fillDataLevels(node.m_right, dls, level+1);
	}


	// --- END AVL
	
	// --- FIB
	
	// --- END FIB
}
