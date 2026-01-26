package com.terracetech.tims.webmail.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ObjectUtil {
	public static <K, V> Map<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	public static <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}

	public static <T> List<T> newLinkedList() {
		return new LinkedList<T>();
	}

	public static <T> Set<T> newHashSet() {
		return new HashSet<T>();
	}

	public static <T> Set<T> newTreeSet() {
		return new TreeSet<T>();
	}

	public static <T> T[] arr(T... objects) {
		return objects;
	}

	public static boolean[] booleans(boolean... booleans) {
		return booleans;
	}

	public static byte[] bytes(byte... bytes) {
		return bytes;
	}

	public static short[] shorts(short... shorts) {
		return shorts;
	}

	public static int[] ints(int... ints) {
		return ints;
	}

	public static long[] longs(long... longs) {
		return longs;
	}

	public static char[] chars(char... chars) {
		return chars;
	}

	public static float[] floats(float... floats) {
		return floats;
	}

	public static double[] doubles(double... doubles) {
		return doubles;
	}
}
