package algorithm;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Demo {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("Bao đóng: " + AttributeClosure.findAttributeClosure(createInputAttributes(sc),
				createAttributes(sc), createFunctionalDependencies(sc)));

		sc.close();
	}

	public static Multimap<Set<Character>, Set<Character>> createFunctionalDependencies(Scanner sc) {
		Multimap<Set<Character>, Set<Character>> functionalDependencies = ArrayListMultimap.create();

		System.out.print("Nhập số lượng phụ thuộc hàm: ");
		int countFunction = sc.nextInt();
		sc.nextLine();

		for (int i = 1; i <= countFunction; i++) {
			System.out.println("Nhập phụ thuộc hàm thứ " + i + ": ");
			Set<Character> start = new HashSet<>();
			Set<Character> end = new HashSet<>();

			System.out.print("Nhập các thuộc tính bên trái: ");

			for (Character attribute : sc.nextLine().toCharArray()) {
				if (attribute != ' ') {
					start.add(attribute);
				}
			}

			System.out.print("Nhập các thuộc tính bên phải: ");

			for (Character attribute : sc.nextLine().toCharArray()) {
				if (attribute != ' ') {
					end.add(attribute);
				}
			}
			System.out.println();
			functionalDependencies.put(start, end);
		}
		System.out.println();
		return functionalDependencies;
	}

	/**
	 * Phương thức để tạo ra tập các thuộc tính của quan hệ
	 *
	 * @return Tập thuộc tính của quan hệ
	 */
	public static Set<Character> createAttributes(Scanner sc) {
		Set<Character> attributes = new HashSet<>();

		System.out.print("Nhập các thuộc tính của quan hệ: ");
		for (Character attribute : sc.nextLine().toCharArray()) {
			if (attribute != ' ') {
				attributes.add(attribute);
			}
		}
		System.out.println();
		return attributes;
	}

	/**
	 * Phương thức để tạo ra tập các thuộc tính của quan hệ
	 *
	 * @return Tập thuộc tính của quan hệ
	 */
	public static Set<Character> createInputAttributes(Scanner sc) {
		Set<Character> inputAttributes = new HashSet<>();
		System.out.print("Nhập các thuộc tính xuất phát: ");

		String input = sc.nextLine();
		for (Character attribute : input.toCharArray()) {
			if (attribute != ' ') {
				inputAttributes.add(attribute);
			}
		}
		System.out.println();
		return inputAttributes;
	}
}