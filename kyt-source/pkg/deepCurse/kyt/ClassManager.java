package pkg.deepCurse.kyt;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.HashMap;

import qj.util.lang.DynamicClassLoader;

/**
 * 
 * @author u1d
 *
 * @param <K>  access key, typically the name of the class for simplicity
 * @param <CT> class type, supply InternalReloadable if you are unsure of what
 *             to use
 */
public class ClassManager<K, CT> {

	private HashMap<K, Class<CT>> classMap;
	private ClassLoader classLoader;
	private URLClassLoader urlClassLoader;

	public ClassManager() {
		classLoader = getClass().getClassLoader();
		classMap = new HashMap<K, Class<CT>>();
	}

	public void add(K key, String path) throws ClassNotFoundException {
		Class<CT> loadedMyClass = (Class<CT>) classLoader.loadClass(path);
		classMap.put(key, loadedMyClass);
	}

	public void addFile(K key, String name, File file) throws ClassNotFoundException, IOException {
		classMap.put(key, (Class<CT>) new DynamicClassLoader(file.getPath()).load(name));
//		Class<CT> loadedMyClass = (Class<CT>) Class.forName(name, true, urlClassLoader.newInstance(new URL[] {file.toURI().toURL()}));
//		classMap.put(key, loadedMyClass);
	}

	public void remove(String path) {

	}

	public void removeFile(File file) {

	}

	/**
	 * This reloadable is for internal use, if you wish to add to its functionality,
	 * simply create a new interface that implements this one (InternalReloadable)
	 * 
	 * @author u1d
	 */
	public interface InternalReloadable<K, V> {
		/**
		 * a data bus for use with dumping data that need to persist across reloads
		 * 
		 * @return
		 */
		default public HashMap<K, V> dump() {
			return null;
		}

		/**
		 * a data bus for use with loading data that need to persist across reloads
		 * 
		 * @param bulk
		 */
		default public void load(HashMap<K, V> dBus) {
		}

	}

	public Class<CT> getClass(K key) {
		return classMap.get(key);
	}

}
