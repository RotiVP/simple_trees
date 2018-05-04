package rot.simpletrees.model;

public interface Tree <K extends Comparable<K>, V> {

	static public class Data <K extends Comparable<K>, V> {

		public Data(K key, V value) 
		{
			m_key = key;
			m_value = value;
		}

		public final	K	m_key;
		public			V	m_value;
	}

	static public interface Traverser<K extends Comparable<K>, V> 
	{
		void selected(Tree.Data<K, V> curData);
	}

	Data<K, V> search(K key);
	void insert(Data<K, V> data);
	void remove(K key);
	int size();
}
