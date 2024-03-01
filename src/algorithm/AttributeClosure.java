package algorithm;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class AttributeClosure {
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
		Multimap<Set<Character>, Set<Character>> functionalDependencies = ArrayListMultimap.create();
		Set<Character> lhs1 = new HashSet<>();
		lhs1.add('A');
		lhs1.add('B');
		Set<Character> rhs1 = new HashSet<>();
		rhs1.add('C');
		functionalDependencies.put(lhs1, rhs1);

		Set<Character> lhs2 = new HashSet<>();
		lhs2.add('F');
		lhs2.add('C');
		Set<Character> rhs2 = new HashSet<>();
		rhs2.add('B');
		functionalDependencies.put(lhs2, rhs2);

		Set<Character> lhs3 = new HashSet<>();
		lhs3.add('B');
		lhs3.add('C');
		Set<Character> rhs3 = new HashSet<>();
		rhs3.add('A');
		rhs3.add('D');
		functionalDependencies.put(lhs3, rhs3);

		Set<Character> lhs4 = new HashSet<>();
		lhs4.add('D');
		Set<Character> rhs4 = new HashSet<>();
		rhs4.add('E');
		functionalDependencies.put(lhs4, rhs4);

		Set<Character> inputAttributes = new HashSet<>();
		inputAttributes.add('A');
		inputAttributes.add('B');

		// In kết quả
		System.out.println("Tập thuộc tính xuất phát: " + inputAttributes);
		System.out.println("Tập thuộc tính của quan hệ: " + attributes);
		System.out.println("Tập phụ thuộc hàm: " + functionalDependencies);
		System.out.println("\nBao đóng: " + findAttributeClosure(inputAttributes, attributes, functionalDependencies));
	}

	/**
	 * Hàm tìm bao đóng của một tập thuộc tính đối với tập phụ thuộc hàm(Slide
	 * chương 6 - tr14)
	 * 
	 * @param inputAttributes        Các thuộc tính xuất phát
	 * @param attributes             Tập thuộc tính U
	 * @param functionalDependencies Tập các phụ thuộc hàm F trên U
	 * @return Bao đóng của tập thuộc tính U đối với tập phụ thuộc hàm F trên U
	 */
	public static Set<Character> findAttributeClosure(Set<Character> inputAttributes, Set<Character> attributes,
			Multimap<Set<Character>, Set<Character>> functionalDependencies) {
		// Bao đóng cần tìm
		Set<Character> closure = new HashSet<>(inputAttributes);

		boolean changed;

		do {
			// Xác định tại vòng lặp hiện tại, bao đóng có thay đổi hay không
			changed = false;

			// Duyệt qua tất cả các phụ thuộc hàm bằng vòng lặp for_each
			for (Map.Entry<Set<Character>, Set<Character>> entry : functionalDependencies.entries()) {
				// Tập thuộc tính bên trái của quan hệ
				Set<Character> lhs = entry.getKey();
				// Tập thuộc tính bên phải của quan hệ
				Set<Character> rhs = entry.getValue();

				// Kiểm tra xem lhs có thuộc closure không
				if (closure.containsAll(lhs)) {
					// Nếu thuộc tính nào của rhs không thuộc closure, thì thêm vào closure và đánh
					// dấu đã thay đổi
					for (Character character : rhs) {
						if (!closure.contains(character)) {
							closure.add(character);
							changed = true;
						}
					}
				}
			}
		} while (changed);
		// Trả về bao đóng tìm được
		return closure;
	}
}
