package algorithm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class FindMinimalCover {
	public static void main(String[] args) {
		// Tập thuộc tính ban đầu
		Set<Character> attributes = new HashSet<>();
		attributes.add('A');
		attributes.add('B');
		attributes.add('C');
		attributes.add('D');
		attributes.add('E');
		attributes.add('F');
		attributes.add('G');

		// Tập phụ thuộc hàm ban đầu
		Multimap<Set<Character>, Set<Character>> functionalDependencies = ArrayListMultimap.create();
		Set<Character> lhs11 = new HashSet<>();
		lhs11.add('A');
		Set<Character> rhs11 = new HashSet<>();
		rhs11.add('B');
		functionalDependencies.put(lhs11, rhs11);

		Set<Character> lhs12 = new HashSet<>();
		lhs12.add('A');
		lhs12.add('B');
		lhs12.add('C');
		lhs12.add('D');
		Set<Character> rhs12 = new HashSet<>();
		rhs12.add('E');
		functionalDependencies.put(lhs12, rhs12);

		Set<Character> lhs13 = new HashSet<>();
		lhs13.add('E');
		lhs13.add('F');
		Set<Character> rhs13 = new HashSet<>();
		rhs13.add('G');
		functionalDependencies.put(lhs13, rhs13);

		Set<Character> lhs14 = new HashSet<>();
		lhs14.add('A');
		lhs14.add('C');
		lhs14.add('D');
		lhs14.add('F');
		Set<Character> rhs14 = new HashSet<>();
		rhs14.add('E');
		rhs14.add('G');
		functionalDependencies.put(lhs14, rhs14);

		// In kết quả
		System.out.println("Tập thuộc tính của quan hệ: " + attributes);
		System.out.println("Tập phụ thuộc hàm F: " + functionalDependencies);

		System.out.println("\nF0 = " + functionalDependencies);
		System.out.println("Key = " + findMinimalCover(attributes, functionalDependencies));
	}

	/**
	 * Tìm phủ tối thiểu của 1 tập phụ thuộc hàm
	 * 
	 * @param attributes             Tập thuộc tính
	 * @param functionalDependencies Tập phụ thuộc hàm
	 * @return Phủ tối thiểu của tập phụ thuộc hàm
	 */
	public static Multimap<Set<Character>, Set<Character>> findMinimalCover(Set<Character> attributes,
			Multimap<Set<Character>, Set<Character>> functionalDependencies) {
		// Phủ tối thiểu cần tìm của tập phụ thuộc hàm
		Multimap<Set<Character>, Set<Character>> answer = ArrayListMultimap.create();

		// Tập bên trái của tập phụ thuộc hàm
		Set<Set<Character>> keySets = functionalDependencies.keySet();

		// Bước 1: Biến đổi F về dạng F1 = {Li -> Aj}
		// MultiMap F1 chứa các phụ thuộc của tập F1
		Multimap<Set<Character>, Set<Character>> F1 = ArrayListMultimap.create();
		// Duyệt qua từng phần tử bên trái
		for (Set<Character> key : keySets) {
			// Lấy về tập value tương ứng với key
			Collection<Set<Character>> valueKey = functionalDependencies.get(key);
			// Xét từng value riêng trong tập value
			for (Set<Character> valueCheck : valueKey) {
				// Duyệt qua từng thuộc tính trong value
				for (Character value : valueCheck) {
					Set<Character> valueInsert = new HashSet<>();
					valueInsert.add(value);
					// Check nếu chưa có bộ key - thuộc tính đang xét thì thêm vào F1
					if (!F1.containsEntry(key, valueInsert)) {
						F1.put(key, valueInsert);
					}
				}
			}
		}
		System.out.println("F1 = " + F1);

		// Bước 2: Loại bỏ thuộc tính thừa trong vế trái của các phụ thuộc hàm
		// MultiMap F2 chứa các phụ thuộc của F1 sau khi giản ước vế trái
		Multimap<Set<Character>, Set<Character>> F2 = ArrayListMultimap.create(F1);
		boolean changed;
		do {
			Multimap<Set<Character>, Set<Character>> F21 = ArrayListMultimap.create(F2);
			changed = false;
			Set<Set<Character>> keySetF2 = F21.keySet();
			// Duyệt qua từng phần tử bên trái
			for (Set<Character> keySet : keySetF2) {
				if (changed == true)
					continue;
				if (keySet.size() > 1) {
					// Tập các value tương ứng với phần tử key
					Collection<Set<Character>> valueKeySet = F21.get(keySet);
					// Xét từng cặp key - value
					for (Set<Character> valueKey : valueKeySet) {
						if (changed == true)
							continue;
						// Duyệt qua từng thuộc tính của key
						for (Character character : keySet) {
							if (changed == true)
								continue;
							// Tập các thuộc tính vế trái của phụ thuộc
							Set<Character> keyCheck = new HashSet<>(keySet);
							// Loại bỏ thuộc tính đang xét ra khỏi key
							keyCheck.remove(character);
							// Kiểm tra sự tương đương của 2 phụ thuộc hàm
							// Tìm bao đóng của key sau khi loại bỏ thuộc tính và kiểm tra có chứa value hay không
							if (AttributeClosure.findAttributeClosure(keyCheck, attributes, F21)
									.containsAll(valueKey)) {
								changed = true;
								F2.remove(keySet, valueKey);
								if (!F2.containsEntry(keyCheck, valueKey)) {
									F2.put(keyCheck, valueKey);
								}
								continue;
							}
						}
					}
				}
			}
		} while (changed);

		System.out.println("F2 = " + F2);

		// Bước 3: Loại bỏ phụ thuộc hàm dư thừa
		answer = FindNonRedundant.findNonRedundant(attributes, F2);

		return answer;
	}
}
