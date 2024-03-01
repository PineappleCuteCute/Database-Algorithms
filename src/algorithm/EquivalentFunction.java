package algorithm;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class EquivalentFunction {
	public static void main(String[] args) {
		// Tập thuộc tính ban đầu
		Set<Character> attributes = new HashSet<>();
		attributes.add('A');
		attributes.add('B');
		attributes.add('C');
		attributes.add('D');
		attributes.add('E');
		attributes.add('F');

		// Tập phụ thuộc hàm ban đầu
		Multimap<Set<Character>, Set<Character>> functionalDependencies1 = ArrayListMultimap.create();
		Set<Character> lhs11 = new HashSet<>();
		lhs11.add('A');
		lhs11.add('B');
		Set<Character> rhs11 = new HashSet<>();
		rhs11.add('C');
		functionalDependencies1.put(lhs11, rhs11);

		Set<Character> lhs12 = new HashSet<>();
		lhs12.add('D');
		Set<Character> rhs12 = new HashSet<>();
		rhs12.add('E');
		rhs12.add('F');
		functionalDependencies1.put(lhs12, rhs12);

		Set<Character> lhs13 = new HashSet<>();
		lhs13.add('C');
		Set<Character> rhs13 = new HashSet<>();
		rhs13.add('D');
		rhs13.add('B');
		functionalDependencies1.put(lhs13, rhs13);

		Multimap<Set<Character>, Set<Character>> functionalDependencies2 = ArrayListMultimap.create();
		Set<Character> lhs21 = new HashSet<>();
		lhs21.add('A');
		lhs21.add('C');
		Set<Character> rhs21 = new HashSet<>();
		rhs21.add('B');
		functionalDependencies2.put(lhs21, rhs21);

		Set<Character> lhs22 = new HashSet<>();
		lhs22.add('D');
		Set<Character> rhs22 = new HashSet<>();
		rhs22.add('E');
		rhs22.add('F');
		functionalDependencies2.put(lhs22, rhs22);

		Set<Character> lhs23 = new HashSet<>();
		lhs23.add('B');
		Set<Character> rhs23 = new HashSet<>();
		rhs23.add('D');
		rhs23.add('C');
		functionalDependencies2.put(lhs23, rhs23);

		// In kết quả
		System.out.println("Tập thuộc tính của quan hệ: " + attributes);
		System.out.println("Tập phụ thuộc hàm F: " + functionalDependencies1);
		System.out.println("Tập phụ thuộc hàm G: " + functionalDependencies2);

		System.out.println("\nHai tập phụ thuộc hàm tương đương: "
				+ isEquivalentFunction(attributes, functionalDependencies1, functionalDependencies2));
	}

	/**
	 * Phương thức xác định 2 tập phụ thuộc hàm có tương đương nhau hay không
	 * 
	 * @param attributes              Tập thuộc tính
	 * @param functionalDependencies1 Tập phụ thuộc hàm F
	 * @param functionalDependencies2 Tập phụ thuộc hàm G
	 * @return Kết quả của việc so sánh sự tương đương 2 tập phụ thuộc hàm F và G
	 */
	@SuppressWarnings("unlikely-arg-type")
	public static boolean isEquivalentFunction(Set<Character> attributes,
			Multimap<Set<Character>, Set<Character>> functionalDependencies1,
			Multimap<Set<Character>, Set<Character>> functionalDependencies2) {
		// Tập nguồn của tập phụ thuộc hàm F
		Set<Set<Character>> F = functionalDependencies1.keySet();
		// Xét từng phụ thuộc hàm của tập phụ thuộc hàm F
		for (Set<Character> set : F) {
			// Tìm bao đóng của phụ thuộc hàm đang xét trên G
			Set<Character> value = AttributeClosure.findAttributeClosure(set, attributes, functionalDependencies2);
			// Nếu bao đóng không chứa tập đích của phụ thuộc hàm
			if (!value.containsAll(functionalDependencies1.get(set))) {
				// Kết thúc thuật toán và trả về false
				return false;
			}
		}

		// Tập nguồn của tập phụ thuộc hàm G
		Set<Set<Character>> G = functionalDependencies2.keySet();
		// Xét từng phụ thuộc hàm của tập phụ thuộc hàm G
		for (Set<Character> set : G) {
			// Tìm bao đóng của phụ thuộc hàm trên F
			Set<Character> value = AttributeClosure.findAttributeClosure(set, attributes, functionalDependencies1);
			if (!value.containsAll(functionalDependencies2.get(set))) {
				return false;
			}
		}

		return true;
	}
}
