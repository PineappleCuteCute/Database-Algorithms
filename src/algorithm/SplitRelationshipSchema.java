package algorithm;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class SplitRelationshipSchema {
	public static void main(String[] args) {
		// Tập thuộc tính ban đầu
		Set<Character> attributes = new HashSet<>();
		attributes.add('A');
		attributes.add('B');
		attributes.add('C');
		attributes.add('D');

		// Tập phụ thuộc hàm ban đầu
		Multimap<Set<Character>, Set<Character>> functionalDependencies = ArrayListMultimap.create();
		Set<Character> lhs1 = new HashSet<>();
		lhs1.add('A');
		Set<Character> rhs1 = new HashSet<>();
		rhs1.add('C');
		functionalDependencies.put(lhs1, rhs1);

		Set<Character> lhs2 = new HashSet<>();
		lhs2.add('B');
		Set<Character> rhs2 = new HashSet<>();
		rhs2.add('C');
		functionalDependencies.put(lhs2, rhs2);

		Set<Character> lhs3 = new HashSet<>();
		lhs3.add('D');
		lhs3.add('C');
		Set<Character> rhs3 = new HashSet<>();
		rhs3.add('B');
		functionalDependencies.put(lhs3, rhs3);

		Set<Character> lhs4 = new HashSet<>();
		lhs4.add('C');
		Set<Character> rhs4 = new HashSet<>();
		rhs4.add('D');
		functionalDependencies.put(lhs4, rhs4);

		// Kết quả của phép tách
		Set<Set<Character>> split = new HashSet<>();

		Set<Character> split1 = new HashSet<>();
		split1.add('A');
		split1.add('B');
		split.add(split1);

		Set<Character> split2 = new HashSet<>();
		split2.add('D');
		split2.add('B');
		split.add(split2);

		Set<Character> split3 = new HashSet<>();
		split3.add('A');
		split3.add('B');
		split3.add('C');
		split.add(split3);

		Set<Character> split4 = new HashSet<>();
		split4.add('B');
		split4.add('C');
		split4.add('D');
		split.add(split4);

		// In kết quả
		System.out.println("Tập thuộc tính của quan hệ: " + attributes);
		System.out.println("Tập phụ thuộc hàm F: " + functionalDependencies);
		System.out.println("Tập quan hệ sau khi tách: " + split);

		System.out.println("\nMất mát thông tin: "
				+ SplitRelationshipSchema.splitRelationshipSchema(attributes, functionalDependencies, split));
	}

	/**
	 * Hàm kiểm tra tính mất mát thông tin
	 * 
	 * @param inputAttributes        Tập thuộc tính ban đầu
	 * @param functionalDependencies Tập phụ thuộc hàm
	 * @param splitAttributes        Tập quan hệ sau khi tách
	 * @return Kết quả
	 */
	public static boolean splitRelationshipSchema(Set<Character> inputAttributes,
			Multimap<Set<Character>, Set<Character>> functionalDependencies, Set<Set<Character>> splitAttributes) {
		// k - số hàng, n - số cột của bảng
		int k = splitAttributes.size();
		int n = inputAttributes.size();

		// Bảng
		String[][] table = new String[k][n];

		// Bước 1: Tạo dữ liệu ban đầu cho bảng
		int i = 0;
		// Duyệt qua từng sơ đồ con Ri
		for (Set<Character> split : splitAttributes) {
			int j = 0;
			// Duyệt qua từng thuộc tính Aj của quan hệ
			for (Character character : inputAttributes) {
				// Nếu Aj là thuộc tính của Ri
				if (split.contains(character)) {
					// Điền aj vào ô (i, j)
					table[i][j] = "a" + (j + 1) + " ";
				} else {
					// Nếu không điền bij vào ô (i, j)
					table[i][j] = "b" + (i + 1) + (j + 1) + " ";
				}
				j++;
			}
			i++;
		}

		// Kiểm tra tính đồng nhất
		// Mảng chứa các thuộc tính
		Character[] inputAttributesArray = inputAttributes.toArray(new Character[0]);
		boolean changed;
		do {
			changed = false;
			// Tập vế trái của các phụ thuộc hàm
			Set<Set<Character>> keySets = functionalDependencies.keySet();

			// Xét duyệt qua từng phụ thuộc hàm
			for (Set<Character> keySet : keySets) {
				// Lấy vế phải của phụ thuộc hàm đang xét
				Set<Character> valueSet = functionalDependencies.get(keySet).iterator().next();
				// Xét từng cặp các hàng trong bảng
				for (i = 0; i < k; i++) {
					for (int j = i + 1; j < k; j++) {
						boolean isIdentical = true;

						// Xét điều kiện t1[X] = t2[X]
						for (Character character : keySet) {
							int index = findIndex(inputAttributesArray, character);
							if (!table[i][index].equals(table[j][index])) {
								isIdentical = false;
								break;
							}
						}

						// Đồng nhất t1[Y] = t2[Y]
						if (isIdentical) {
							for (Character character : valueSet) {
								int index = findIndex(inputAttributesArray, character);
								if (!table[i][index].equals(table[j][index])) {
									if (table[i][index].startsWith("a")) {
										table[j][index] = table[i][index];
									} else if (table[j][index].startsWith("a")) {
										table[i][index] = table[j][index];
									} else {
										table[j][index] = table[i][index];
									}
									changed = true;

//									for (int h = 0; h < k; h++) {
//										for (int l = 0; l < n; l++) {
//											System.out.print(table[h][l] + " ");
//										}
//										System.out.print("\n");
//									}
//									System.out.println();
								}
							}

						}
					}
				}
			}
		} while (changed);

		// Bước B.n: Kiểm tra tính mất mát thông tin
		boolean isLossless = true;
		for (i = 1; i < k; i++) {
			if (!arrayEquals(table[0], table[i])) {
				isLossless = false;
				break;
			}
		}

		return isLossless;
	}

	/**
	 * Hàm tìm vị trí của một ký tự trong một mảng ký tự
	 * 
	 * @param array     Mảng ký tự
	 * @param character Ký tự cần tìm
	 * @return Vị trí của ký tự trong mảng, -1 nếu không tìm thấy
	 */
	private static int findIndex(Character[] array, Character character) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(character)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Hàm so sánh hai mảng hai chiều
	 * 
	 * @param array1 Mảng 1
	 * @param array2 Mảng 2
	 * @return True nếu hai mảng giống nhau, False nếu khác nhau
	 */
	private static boolean arrayEquals(String[] array1, String[] array2) {
		if (array1.length != array2.length) {
			return false;
		}
		for (int i = 0; i < array1.length; i++) {
			if (!array1[i].equals(array2[i])) {
				return false;
			}
		}
		return true;
	}
}
