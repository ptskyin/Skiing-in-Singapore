import java.io.FileNotFoundException;
import java.util.LinkedList;

/**
 * Created by skyin on 12/18/2016.
 */
public class Ski {
	private final int[][] map;
	private final int[][] hMap;
	private final LinkedList<SkiNode> goodHeads = new LinkedList<>();
	private SkiNode bestHead, bestTail;

	public Ski(String s) throws FileNotFoundException {
		map = MatrixHelper.readMatrix(s);
		hMap = new int[map.length][map[0].length];
	}

	private void findBestRout() throws Exception {
		for (SkiNode head : goodHeads) {
			SkiNode tail = findBestTail(head);
			if (tail == null) {
				throw new Exception("Incomplete route for head: " + head.i + "," + head.j + "," + head.v + "," + head.level);
			}
			if (bestHead == null || bestTail == null ||
					head.v - tail.v > bestHead.v - bestTail.v) {
				bestHead = head;
				bestTail = tail;
			}
		}
	}

	/**
	 * Build a tree of routes that will only direct to best tails
	 * (level for parent child only have diff of 1 and min tail value)
	 *
	 * @param head current starting pos
	 * @return best tail
	 */
	private SkiNode findBestTail(SkiNode head) {
		SkiNode tail, minTail;
		minTail = updateNextNodeToBestTail(head, head.i - 1, head.j, 0);
		tail = updateNextNodeToBestTail(head, head.i, head.j - 1, 1);
		if (tail != null && (minTail == null || tail.v < minTail.v)) {
			minTail = tail;
		}
		tail = updateNextNodeToBestTail(head, head.i + 1, head.j, 2);
		if (tail != null && (minTail == null || tail.v < minTail.v)) {
			minTail = tail;
		}
		tail = updateNextNodeToBestTail(head, head.i, head.j + 1, 3);
		if (tail != null && (minTail == null || tail.v < minTail.v)) {
			minTail = tail;
		}
		return minTail;
	}

	/**
	 * level for parent child only have diff of 1
	 *
	 * @param head   current starting pos
	 * @param nextI  pos next to head, i
	 * @param nextJ  pos next to head, j
	 * @param nextId denoting the relevant pos to head: up, down, left or right
	 * @return best tail
	 */
	private SkiNode updateNextNodeToBestTail(SkiNode head, int nextI, int nextJ, int nextId) {
		if (!MatrixHelper.isPosValid(nextI, nextJ, map)) {
			return null;
		}
		if (head.level - 1 != hMap[nextI][nextJ]) {
			return null;
		}
		head.next[nextId] = new SkiNode(map[nextI][nextJ], nextI, nextJ, head);
		head.next[nextId].level = hMap[nextI][nextJ];
		if (head.next[nextId].level == 1) {
			return head.next[nextId];
		}
		return findBestTail(head.next[nextId]);
	}

	/**
	 * Treat unexplored pos in hMap as head and build tree. Update good heads.
	 */
	private void constructHMap() {
		for (int i = 0; i < hMap.length; i++) {
			for (int j = 0; j < hMap[0].length; j++) {
				if (hMap[i][j] == 0) {
					SkiNode head = new SkiNode(map[i][j], i, j, null);
					buildSkiTree(head);
					updateSkiTreeLevel(head);
					if (goodHeads.isEmpty()) {
						goodHeads.add(new SkiNode(head));
					} else if (goodHeads.getFirst().level == head.level) {
						goodHeads.add(new SkiNode(head));
					} else if (goodHeads.getFirst().level < head.level) {
						goodHeads.clear();
						goodHeads.add(new SkiNode(head));
					}
				}
			}
		}
	}

	/**
	 * Compute the level of nodes and update hMap.
	 *
	 * @param head head pos of the tree
	 */
	private void updateSkiTreeLevel(SkiNode head) {
		for (SkiNode nextNode : head.next) {
			if (nextNode != null) {
				updateSkiTreeLevel(nextNode);
				head.level = Math.max(head.level, nextNode.level + 1);
			}
		}
		head.level = Math.max(head.level, 1);
		hMap[head.i][head.j] = head.level;
	}

	/**
	 * Build a tree of all routes with head as starting position
	 *
	 * @param head current starting pos
	 */
	private void buildSkiTree(SkiNode head) {
		updateNextNode(head, head.i - 1, head.j, 0);
		updateNextNode(head, head.i, head.j - 1, 1);
		updateNextNode(head, head.i + 1, head.j, 2);
		updateNextNode(head, head.i, head.j + 1, 3);
	}

	/**
	 * Check if the pos is a valid next pos of head and build branch for the pos.
	 * If the pos has been analyzed before in hMap, the branch will be ignored with updating the head level.
	 *
	 * @param head   current starting pos
	 * @param nextI  pos next to head, i
	 * @param nextJ  pos next to head, j
	 * @param nextId denoting the relevant pos to head: up, down, left or right
	 */
	private void updateNextNode(SkiNode head, int nextI, int nextJ, int nextId) {
		if (!MatrixHelper.isPosValid(nextI, nextJ, map)) {
			return;
		}
		if (head.v <= map[nextI][nextJ]) {
			return;
		}
		if (hMap[nextI][nextJ] != 0) {
			head.level = Math.max(head.level, hMap[nextI][nextJ] + 1);
		} else {
			head.next[nextId] = new SkiNode(map[nextI][nextJ], nextI, nextJ, head);
			buildSkiTree(head.next[nextId]);
		}
	}

	public static void main(String[] args) throws Exception {
		Ski ski = new Ski("map.txt");
//		MatrixHelper.printMatrix(mSki.map);
		ski.constructHMap();
		ski.findBestRout();

		System.out.println("");
		ski.bestTail.printBackward();
		int height = ski.bestHead.v - ski.bestTail.v;
		System.out.println("length=" + ski.bestHead.level + ", drop=" + height);
	}
}
