package rot.simpletrees.model;

import java.util.ArrayList;

public class AVLTree <K extends Comparable<K>, V> 
	extends BinTree <K, V> {

	// --- NODE ---
	
	static class Node <K extends Comparable<K>, V>
		extends BinTree.Node<K, V> {

		public Node(BinTree.Data<K, V> data)
		{
			super(data);
		}
		
		byte m_height = 1; //пустой узел имеет высоту 1
	}
	@Override
	protected Node<K, V> createNode(Data<K, V> data)
	{
		return new Node<K, V>(data);
	}

	// --- END NODE ---

	public static class DataLevelsList<K extends Comparable<K>, V>
		extends ArrayList<ArrayList<Data<K, V>>> {

		public DataLevelsList(int initCap) 
		{
			super(initCap);
			while(initCap-- > 0) add(new ArrayList<Data<K, V>>());
		}
   	}
	//convert to avl node
	protected Node<K, V> toAvln( BinTree.Node<K, V> btnode )
	{
		return (Node<K, V>)btnode;
	}

	// --- CTORS
	
	public AVLTree() {}

	// --- END CTORS
	
	// только эти методы работают с высотой, поэтому рационально проводить конвертацию только здесь
	byte getHeight(BinTree.Node<K, V> node)
	{
		return (node != null) ? toAvln(node).m_height : 0;
	}
	void fixHeight(BinTree.Node<K, V> node)
	{
		int lh = getHeight(node.m_left);
		int rh = getHeight(node.m_right);
		toAvln(node).m_height = (byte)( ((lh > rh) ? lh : rh)+1 );
	}

	byte getBalance(BinTree.Node<K, V> root)
	{
		return (byte)(
				getHeight(root.m_right) 
				- 
				getHeight(root.m_left)
				);
	}
	@Override
	protected BinTree.Node<K, V> balance(BinTree.Node<K, V> root)
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
	BinTree.Node<K, V> rotateR(BinTree.Node<K, V> root)
	{
		BinTree.Node<K, V> newRoot = root.m_left;
		root.m_left = newRoot.m_right;
		newRoot.m_right = root;

		fixHeight(root);
		fixHeight(newRoot);
		return newRoot;
	}
	BinTree.Node<K, V> rotateL(BinTree.Node<K, V> root)
	{
		BinTree.Node<K, V> newRoot = root.m_right;
		root.m_right = newRoot.m_left;
		newRoot.m_left = root;

		fixHeight(root);
		fixHeight(newRoot);
		return newRoot;
	}

	public DataLevelsList<K, V> getDataLevels()
	{	
		/*
		inorderTraversal(m_root, new CallBack<K, V>() { 
			@Override 
			void action(BinTree.Node<K, V> node) {
				System.out.println(node.m_data.m_key); 
			}
		});
		*/
		DataLevelsList<K, V> dls = new DataLevelsList<>(getHeight(m_root));
		fillDataLevels(m_root, dls, 0);
		return dls;
	}
	void fillDataLevels(
			BinTree.Node<K, V> node,
			DataLevelsList<K, V> dls, 
			int level)
	{
		if(level >= dls.size()) return;

		fillDataLevels((node==null) ? node : node.m_left, dls, level+1);

		Data<K, V> data;
		if(node == null) data = null;
		else data = node.m_data;
		dls.get(level).add(data);

		fillDataLevels((node==null) ? node : node.m_right, dls, level+1);
	}

	public byte height()
	{
		return getHeight(m_root);
	}
}
