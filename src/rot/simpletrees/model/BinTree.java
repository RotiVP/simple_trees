package rot.simpletrees.model;

import java.util.ArrayList;
import java.util.Collection;

public class BinTree <K extends Comparable<K>, V>
	implements Tree <K, V> {

// --- NODE STUFF ---
	
	static class Node<K extends Comparable<K>, V> 
	{
		public Node(Tree.Data<K, V> data)
		{
			m_data = data;
		}

		Tree.Data<K, V> m_data;
		Node<K, V> m_left = null;
		Node<K, V> m_right = null;
	}
	protected Node<K, V> m_root = null;
	protected Node<K, V> createNode(Tree.Data<K, V> data)	
	{
		return new Node<K, V>(data);
	}

// --- END NODE STUFF ---	

// --- CTORs ---

	public BinTree() {}

// --- END CTORs ---

	protected Node<K, V> getMinimum(Node<K, V> root)
	{
		if(root.m_left == null) return root;
		return getMinimum(root.m_left);
	}
	protected Node<K, V> getMaximum(Node<K, V> root)
	{
		if(root.m_right == null) return root;
		return getMaximum(root.m_right);
	}
	protected Node<K, V> balance(Node<K, V> root)
	{ 
		return root; 
	}
	
	public Tree.Traverser<K, V> m_traverser = null;

// --- USER INTERFACE ---
	
// --- SEARCH --- 

	private Node<K, V> search(K key, Node<K, V> root) 
	{
		// рекурсивная реализация
		if( root == null ) return root;

		if(m_traverser != null) {
			m_traverser.selected(root.m_data);
		}

		int compareResult = key.compareTo(root.m_data.m_key);

		if(compareResult == 0) return root;
		if(compareResult > 0) return search(key, root.m_right);
		return search(key, root.m_left);

		// итеративная реализация
		/*
		 * стек не расширяется для хранения адреса возврата, параметров и прочей информации о вызове
		 */
		/*
		do {
			int compareResult = key.compareTo(root.m_data.m_key);
			if		( compareResult > 0 ) root = root.m_right;
			else if ( compareResult < 0 ) root = root.m_left;
		} while ( root != null || compareResult != 0 )
		return root;
		*/
	}

	@Override
	public Tree.Data<K, V> search(K key)
	{
		Node<K, V> node = search(key, m_root);
		if( node == null ) return null;
		else return node.m_data;
	}

// --- END SEATCH ---
	
// --- INSERT ---

	private Node<K, V> insert( Tree.Data<K, V> data, Node<K, V> root)
	{
		if( root == null ) {
			root = createNode(data); 
			++size;
			return root;
		}
		if(m_traverser != null) {
			m_traverser.selected(root.m_data);
		}

		int compareResult = data.m_key.compareTo(root.m_data.m_key);

		if( compareResult > 0 )			root.m_right = insert(data, root.m_right);
		else if( compareResult < 0 )	root.m_left = insert(data, root.m_left);
		else							root.m_data.m_value = data.m_value;

		return balance(root);
	}
	@Override
	public void insert( Tree.Data<K, V> data )
	{
		m_root = insert( data, m_root );
	}

// --- END INSERT ---

// --- REMOVE ---

	private Node<K, V> remove( K key, Node<K, V> root )
	{
		if( root == null ) return root;

		if(m_traverser != null) {
			m_traverser.selected(root.m_data);
		}

		int compareResult = key.compareTo(root.m_data.m_key);

		if( compareResult > 0 ) root.m_right = remove( key, root.m_right );
		else if( compareResult < 0 ) root.m_left = remove( key, root.m_left );

		else if( compareResult == 0 ) { 
			if ( root.m_left != null && root.m_right != null ) {
				root.m_data = getMinimum(root.m_right).m_data;
				root.m_right = remove(root.m_data.m_key, root.m_right);

			} else {
				if ( root.m_left != null ) root = root.m_left;		
				else root = root.m_right;
				--size;
				return root;
			}
		}
		
		return balance(root);
	}
	@Override
	public void remove( K key )
	{
		m_root = remove( key, m_root );
	}

// --- END REMOVE ---

// --- TRAVERSE ---	

	protected void inorderTraversal( Node<K, V> root, Tree.Traverser<K, V> tr)
	{
		if (root == null) return;
		inorderTraversal(root.m_left, tr);
		tr.selected(root.m_data);
		inorderTraversal(root.m_right, tr);
	}	
	protected void preorderTraversal( Node<K, V> root, Tree.Traverser<K, V> tr)
	{
		if( root == null ) return;

		tr.selected(root.m_data);
		preorderTraversal(root.m_left, tr);
		preorderTraversal(root.m_right, tr);
	}
	protected void postorderTraversal( Node<K, V> root, Tree.Traverser<K, V> tr)
	{
		if( root == null ) return;

		postorderTraversal(root.m_left, tr);
		postorderTraversal(root.m_right, tr);
		tr.selected(root.m_data);
	}

// --- END TRAVERSE ---

	private int size;

	@Override
	public int size()
	{
		return size;
	}
	
	public ArrayList<Tree.Data<K, V>> getDataList()
	{
		ArrayList<Tree.Data<K, V>> list = new ArrayList<>(size());
		fillCollection(list);
		return list;
	}
	protected void fillCollection(Collection<Tree.Data<K, V>> data)
	{
		data.clear();
		fillCollection(m_root, data);
	}
	protected void fillCollection(Node<K, V> root, Collection<Tree.Data<K, V>> data)
	{
		if(root == null) return;
		fillCollection(root.m_left, data);
		data.add(root.m_data);
		fillCollection(root.m_right, data);
	}
}
