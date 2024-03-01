package algorithm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class FindNonRedundant {
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
		attributes.add('H');
		attributes.add('I');
		attributes.add('J');

		// Tập phụ thuộc hàm ban đầu
		Multimap<Set<Character>, Set<Character>> functionalDependencies1 = ArrayListMultimap.create();

		Set<Character> lhs11 = new HashSet<>();
		lhs11.add('A');
		Set<Character> rhs11 = new HashSet<>();
		rhs11.add('C');
		rhs11.add('B');
		rhs11.add('D');
		functionalDependencies1.put(lhs11, rhs11);

		Set<Character> lhs12 = new HashSet<>();
		lhs12.add('D');
		lhs12.add('C');
		Set<Character> rhs12 = new HashSet<>();
		rhs12.add('B');
		functionalDependencies1.put(lhs12, rhs12);

		Set<Character> lhs13 = new HashSet<>();
		lhs13.add('E');
		lhs13.add('F');
		Set<Character> rhs13 = new HashSet<>();
		rhs13.add('G');
		rhs13.add('H');
		functionalDependencies1.put(lhs13, rhs13);

		Set<Character> lhs14 = new HashSet<>();
		lhs14.add('E');
		Set<Character> rhs14 = new HashSet<>();
		rhs14.add('G');
		rhs14.add('I');
		rhs14.add('J');
		functionalDependencies1.put(lhs14, rhs14);

		Set<Character> lhs15 = new HashSet<>();
		lhs15.add('I');
		Set<Character> rhs15 = new HashSet<>();
		rhs15.add('J');
		functionalDependencies1.put(lhs15, rhs15);

		// In kết quả
		System.out.println("Tập thuộc tính của quan hệ: " + attributes);
		System.out.println("Tập phụ thuộc hàm F: " + functionalDependencies1);

		System.out.println("\nTập phụ thuộc hàm không dư thừa: " + findNonRedundant(attributes, functionalDependencies1));
	}

	/**
	 * Phương thức tìm tập phụ thuộc hàm không dư thừa
	 * 
	 * @param attributes             Tập thuộc tính U
	 * @param functionalDependencies Tập phụ thuộc hàm F0
	 * @return 						 Tập phụ thuộc hàm không dư thừa
	 */
	public static Multimap<Set<Character>, Set<Character>> findNonRedundant(Set<Character> attributes,
			Multimap<Set<Character>, Set<Character>> functionalDependencies) {
		// Tập phụ thuộc hàm không dư thừa cần tìm: Khởi tạo bằng tập phụ thuộc ban đầu
		Multimap<Set<Character>, Set<Character>> answer = ArrayListMultimap.create(functionalDependencies);
		// Chứa các phụ thuộc hàm dư thừa
		Multimap<Set<Character>, Set<Character>> remove = ArrayListMultimap.create();
		// Chứa vế trái của các phụ thuộc hàm
		Set<Set<Character>> keySets = answer.keySet();

		// Xét từng phụ thuộc hàm trong tập phụ thuộc hàm F0
		for (Set<Character> key : keySets) {
			Multimap<Set<Character>, Set<Character>> value = ArrayListMultimap.create(functionalDependencies);

			Collection<Set<Character>> valueKey = value.get(key);

			for (Set<Character> valueCheck : valueKey) {
				Multimap<Set<Character>, Set<Character>> valueF = ArrayListMultimap.create(functionalDependencies);
				// Loại bỏ phụ thuộc hàm đang xét ra khỏi F0 được F
				valueF.remove(key, valueCheck);

				// Tìm bao đóng của phụ thuộc hàm đang xét trên F
				Set<Character> attributeClosure = AttributeClosure.findAttributeClosure(key, attributes, valueF);
				// Nếu bao đóng chứa các thuộc tính đích tức nó dư thừa
				if (attributeClosure.containsAll(valueCheck)) {
					// Thêm phụ thuộc hàm cần loại bỏ vào remove
					remove.put(key, valueCheck);
				}
			}
		}

		// Loại bỏ các phụ thuộc hàm dư thừa
		Set<Set<Character>> keyRemove = remove.keySet();
		for (Set<Character> key : keyRemove) {
			Collection<Set<Character>> valueRemove = remove.get(key);
			for (Set<Character> value : valueRemove) {
				answer.remove(key, value);
			}
		}

		return answer;
	}
}
