package algorithm;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class MiniumKey {
	public static void main(String[] args) {
		// Tập thuộc tính ban đầu
		Set<Character> attributes = new HashSet<>();
		attributes.add('A');
		attributes.add('B');
		attributes.add('C');
		attributes.add('D');
		attributes.add('E');

		// Tập phụ thuộc hàm ban đầu
		Multimap<Set<Character>, Set<Character>> functionalDependencies = ArrayListMultimap.create();
		Set<Character> lhs1 = new HashSet<>();
		lhs1.add('A');
		lhs1.add('B');
		Set<Character> rhs1 = new HashSet<>();
		rhs1.add('C');
		functionalDependencies.put(lhs1, rhs1);

		Set<Character> lhs2 = new HashSet<>();
		lhs2.add('A');
		lhs2.add('C');
		Set<Character> rhs2 = new HashSet<>();
		rhs2.add('B');
		functionalDependencies.put(lhs2, rhs2);

		Set<Character> lhs3 = new HashSet<>();
		lhs3.add('B');
		lhs3.add('C');
		Set<Character> rhs3 = new HashSet<>();
		rhs3.add('D');
		rhs3.add('E');
		functionalDependencies.put(lhs3, rhs3);

		// In kết quả
		System.out.println("Tập thuộc tính của quan hệ: " + attributes);
		System.out.println("Tập phụ thuộc hàm: " + functionalDependencies);
		System.out.println("\nKhóa tối thiểu: " + findMinimumKey(attributes, functionalDependencies));
	}

	/**
	 * Phương thức tìm khóa tối thiểu(Slide chương 6 - tr18)
	 * 
	 * @param attributes             Tập thuộc tính U
	 * @param functionalDependencies Tập các phụ thuộc hàm F trên U
	 * @return Tập khóa tối thiểu
	 */
	public static Set<Character> findMinimumKey(Set<Character> attributes,
			Multimap<Set<Character>, Set<Character>> functionalDependencies) {
		// Tập khóa tối thiểu cần tìm: Xuất phát là tập thuộc tính U
		Set<Character> minimumKey = new HashSet<>(attributes);

		// Duyệt qua từng thuộc tính của tập thuộc tính U
		for (Character character : attributes) {
			Set<Character> newKey = new HashSet<>(minimumKey);
			// Loại bỏ attribute hiện tại đang xét ra khỏi tập khóa tối thiểu
			newKey.remove(character);

			// Kiểm tra điều kiện tương đương
			// Tìm bao đóng của tập thuộc tính sau khi loại bỏ 1 thuộc tính
			Set<Character> check = AttributeClosure.findAttributeClosure(newKey, attributes, functionalDependencies);

			// Nếu bao đóng khác U thì không thay đổi tập khóa tối thiểu
			if (!(check.containsAll(attributes) && attributes.containsAll(check))) {
				// Thêm thuộc tính đã loại bỏ vào lại tập khóa tối thiểu
				newKey.add(character);
			}
			minimumKey = newKey;
		}
		// Trả về kết quả của hàm
		return minimumKey;
	}

}
