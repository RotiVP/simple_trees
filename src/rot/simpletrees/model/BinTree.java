package rot.simpletrees.model;

public class BinTree <K extends Comparable<K>, V> {	

	static public class Data <K extends Comparable<K>, V> {

		public Data(K key, V value) {
			m_key = key;
			m_value = value;
		}
		public final	K	m_key;
		public			V	m_value;
	}

	static class Node <K extends Comparable<K>, V> {
		Node(	Data<K, V>	data,
				Node<K, V>	left,
				Node<K, V>	right) {
			m_data = data;
			m_left = left;
			m_right = right;
		}

		Node<K, V>	m_left;
		Node<K, V>	m_right;

		Data<K, V>	m_data;	
	}	
	protected Node<K, V> m_root = null;

	static public class CallBack<K extends Comparable<K>, V> {
		void action(Node<K, V> node) { }
	}

	// --- constructors

	/*
	public BinTree( Data<K, V>... init ) 
	{
		for(Data<K, V> e : init)
			insert(e);
	}
	*/
	public BinTree() {}

	// --- end ctors

	// ---

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

// --- user area ---
	
// --- base interface ---
	
// --- SEARCH --- 
	private Node<K, V> search(K key, Node<K, V> root) 
	{
		// рекурсивная реализация
		int compareResult = key.compareTo(root.m_data.m_key);

		if(root == null || compareResult == 0) return root;
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
	public Data<K, V> search(K key)
	{
		Node<K, V> node = search(key, m_root);
		if( node == null ) return null;
		else return node.m_data;
	}
// --- END SEATCH ---
	
// --- INSERT ---
	private Node<K, V> insert( Data<K, V> data, Node<K, V> root)
	{
		if( root == null ) {
			root = new Node<K, V>(data, null, null);
			return root;
		}

		int compareResult = data.m_key.compareTo(root.m_data.m_key);
		if( compareResult > 0 )			root.m_right = insert(data, root.m_right);
		else if( compareResult < 0 )	root.m_left = insert(data, root.m_left);
		else							root.m_data.m_value = data.m_value;

		return balance(root);
	}
	public void insert( Data<K, V> data )
	{
		m_root = insert( data, m_root );
		System.out.println("root - "+m_root);
	}
// --- END INSERT ---

// --- REMOVE ---
	private Node<K, V> remove( K key, Node<K, V> root )
	{
		if( root == null ) return root;

		int compareResult = key.compareTo(root.m_data.m_key);

		if( compareResult > 0 ) root.m_right = remove( key, root.m_right );
		else if( compareResult < 0 ) root.m_left = remove( key, root.m_left );

		else if( compareResult == 0 ) { 
			if ( root.m_left != null && root.m_right != null ) {
				root.m_data = getMinimum(root.m_right).m_data;
				root.m_right = remove(root.m_data.m_key, root.m_right);

			} else if ( root.m_left != null ) root = root.m_left;		
			else root = root.m_right;
		}
		
		return balance(root);
	}
	public void remove( K key )
	{
		remove( key, m_root );
	}
// --- END REMOVE ---

// --- TRAVERSE ---

	private void inorderTraversal( Node<K, V> root, CallBack<K, V> cb)
	{
		if (root == null) return;
		inorderTraversal(root.m_left, cb);
		cb.action(root);
		inorderTraversal(root.m_right, cb);
	}	
	private void preorderTraversal( Node<K, V> root, CallBack<K, V> cb)
	{
		if( root == null ) return;

		cb.action(root);
		preorderTraversal(root.m_left, cb);
		preorderTraversal(root.m_right, cb);
	}
	private void postorderTraversal( Node<K, V> root, CallBack<K, V> cb)
	{
		if( root == null ) return;

		postorderTraversal(root.m_left, cb);
		postorderTraversal(root.m_right, cb);
		cb.action(root);
	}

// --- END TRAVERSE ---

	private void changeCounter( Node<K, V> root, Integer count)
	{
		if (root == null) return;
		changeCounter(root.m_left, count);
		++count;
		changeCounter(root.m_right, count);
	}
	public int getNodeCount()
	{
		Integer count = Integer.valueOf(0);
		changeCounter(m_root, count);
		return count;
	}
}
