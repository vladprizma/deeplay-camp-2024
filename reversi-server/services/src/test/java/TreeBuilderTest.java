import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.bot.TreeBuilder;
import io.deeplay.camp.entity.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TreeBuilderTest {
    @Test
    public void testMaxDepth() {
        BoardService board = new BoardService(new Board()); // Инициализация начального состояния
        int maxDepth = 3; // Установим максимальную глубину

        TreeBuilder.Stats stats;
        stats = TreeBuilder.buildGameTree(board, maxDepth, 1);

        // Проверяем, что максимальная глубина соответствует переданному значению
        Assertions.assertEquals(maxDepth, stats.maxDepth);
    }
}