package cache2;

import cache2.exception.KeyNotFoundException;
import cache2.util.CustomCache;
import cache2.util.ICache;

public class Driver {
    public static void main(String[] args) throws KeyNotFoundException {
        ICache<String, Integer> cache = new CustomCache<>(5);

        cache.put("key1", 100);
        cache.put("key2", 200);
        cache.put("key3", 300);
        cache.put("key4", 400);
        cache.put("key5", 500);
//        System.out.println(cache.get("key1"));
//        System.out.println(cache.get("key3"));
        cache.put("key6", 600);
        System.out.println(cache.get("key1"));
    }
}
