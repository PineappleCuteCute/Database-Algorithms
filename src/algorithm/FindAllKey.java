package algorithm;

import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class FindAllKey {
	public static void main(String[] args) {
		// Tập thuộc tính ban đầu (A, B, C)
		Set<Character> attributes = new HashSet<>();
		attributes.add('A');
		attributes.add('B');
		attributes.add('C');

		// Tập phụ thuộc hàm ban đầu
		// AB -> C
		Multimap<Set<Character>, Set<Character>> functionalDependencies = ArrayListMultimap.create();
		Set<Character> lhs1 = new HashSet<>(Arrays.asList('A', 'B'));
		Set<Character> rhs1 = new HashSet<>(Collections.singletonList('C'));
		functionalDependencies.put(lhs1, rhs1);

		// C -> A
		Set<Character> lhs2 = new HashSet<>(Arrays.asList('C'));
		Set<Character> rhs2 = new HashSet<>(Collections.singletonList('A'));
		functionalDependencies.put(lhs2, rhs2);

		// In kết quả
		System.out.println("Tập thuộc tính của quan hệ: " + attributes);
		System.out.println("Tập phụ thuộc hàm: " + functionalDependencies);

		Set<Set<Character>> value = findAllKeys(attributes, functionalDependencies);

		System.out.println("\nTập tất cả các khóa: ");
		for (Set<Character> key : value) {
			System.out.println(key);
		}
	}

	/**
	 * Hàm tìm tập thuộc tính nguồn TN
	 * 
	 * @param attributes
	 * @param functionalDependencies
	 * @return Một Set các thuộc tính nguồn
	 */
	public static Set<Character> findAttributeStart(Set<Character> attributes,
			Multimap<Set<Character>, Set<Character>> functionalDependencies) {
		Set<Character> attributeStart = new HashSet<>(attributes);
		Set<Character> right = new HashSet<>();

		for (Map.Entry<Set<Character>, Set<Character>> entry : functionalDependencies.entries()) {
			Set<Character> rhs = entry.getValue();

			for (Character rhs1 : rhs) {
				right.add(rhs1);
			}
		}

		for (Character character : right) {
			if (attributeStart.contains(character)) {
				attributeStart.remove(character);
			}
		}
		System.out.println("\nTập thuộc tính nguồn: " + attributeStart);
		return attributeStart;
	}

	/**
	 * Hàm tìm tập thuộc tính trung gian TG
	 * 
	 * @param attributes
	 * @param functionalDependencies
	 * @return Một Set các thuộc tính trung gian
	 */
	public static Set<Character> findAttributeMiddle(Set<Character> attributes,
			Multimap<Set<Character>, Set<Character>> functionalDependencies) {
		Set<Character> attributeMiddle = new HashSet<>();
		Set<Character> left = new HashSet<>();
		Set<Character> right = new HashSet<>();

		for (Map.Entry<Set<Character>, Set<Character>> entry : functionalDependencies.entries()) {
			Set<Character> lhs = entry.getKey();
			Set<Character> rhs = entry.getValue();

			for (Character lhs1 : lhs) {
				left.add(lhs1);
			}

			for (Character rhs1 : rhs) {
				right.add(rhs1);
			}
		}

		for (Character character : right) {
			if (left.contains(character)) {
				attributeMiddle.add(character);
			}
		}
		System.out.println("Tập thuộc tính trung gian: " + attributeMiddle);
		return attributeMiddle;
	}

	/**
	 * Phương thức tìm tất cả các khóa(Slide chương 6 - tr24)
	 * 
	 * @param attributes             Tập thuộc tính U
	 * @param functionalDependencies
	 * @return
	 */
	public static Set<Set<Character>> findAllKeys(Set<Character> attributes,
			Multimap<Set<Character>, Set<Character>> functionalDependencies) {
		Set<Set<Character>> value = new HashSet<>();

		// Xác định tập thuộc tính nguồn và trung gian
		Set<Character> attributeStarts = findAttributeStart(attributes, functionalDependencies);
		Set<Character> attributeMiddles = findAttributeMiddle(attributeStarts, functionalDependencies);

		// Nếu tập thuộc tính trung gian rỗng thì tập các khóa là tập nguồn
		if (attributeMiddles == null) {
			value.add(attributeMiddles);
		} else {
			// Tất cả tập con của tập trung gian
			Set<Set<Character>> subsets = generateSubsets(attributeMiddles);

			// Tìm siêu khóa Si
			// Duyệt qua từng tập con của tập trung gian
			for (Set<Character> set : subsets) {
				// Cấu trúc Set - check là hợp của tập con và tập nguồn
				Set<Character> check = new HashSet<>(attributeStarts);
				for (Character character : set) {
					check.add(character);
				}

				// Bao đóng của check
				Set<Character> attributeClosure = AttributeClosure.findAttributeClosure(check, attributeStarts,
						functionalDependencies);

				// Nếu bao đóng của check bằng với tập thuộc tính U của quan hệ
				if (attributeClosure.containsAll(attributes) && attributes.containsAll(attributeClosure)) {
					// Thêm check vào tập siêu khóa
					value.add(check);
				}
			}
		}

		// Xử lý siêu khóa con
		Set<Set<Character>> keys = new HashSet<>(value);
		for (Set<Character> value1 : value) {
			for (Set<Character> value2 : value) {
				if (value1 != value2 && value2.containsAll(value1)) {
					keys.remove(value2);
					break;
				}
			}
		}
		return keys;
	}

	/**
	 * Phương thức sinh tất cả các tập con của một tập thuộc tính
	 * 
	 * @param attributes Tập thuộc tính cần sinh tập con
	 * @return Một Set các Set chứa tập con của tập thuộc tính
	 */
	public static Set<Set<Character>> generateSubsets(Set<Character> attributes) {
		Set<Set<Character>> subsets = new HashSet<>();
		subsets.add(new HashSet<>()); // Thêm tập rỗng

		for (Character attr : attributes) {
			Set<Set<Character>> newSubsets = new HashSet<>();

			for (Set<Character> subset : subsets) {
				Set<Character> newSubset = new HashSet<>(subset);
				newSubset.add(attr);
				newSubsets.add(newSubset);
			}
			subsets.addAll(newSubsets);
		}
		System.out.println("Tập con của tập trung gian: " + subsets);
		return subsets;
	}

}
