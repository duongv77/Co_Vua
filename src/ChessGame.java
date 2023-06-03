import Utils.Helper;
import entity.ChessCheckmate;
import entity.ChessPiece;
import service.CheckmateService;
import service.StepService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static Utils.Helper.*;

public class ChessGame extends JFrame {
    private static JPanel chessBoard;

    private static ChessPiece selectedPiece;

    /*
        - isTurn = true: lượt quân trắng
        - isTurn = false: lượt quân đen
    */
    private boolean isTurn = true;

    public ChessGame() {
        chessPieces = new ArrayList<>();
        setTitle("Chess Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /*
         * - Vẽ bàn cờ 8 x 8
         * */
        chessBoard = new JPanel();
        chessBoard.setLayout(new GridLayout(8, 8));
        add(chessBoard);

        /*
         * - Tạo các ô cờ
         * */
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel square = new JPanel();
                if ((i + j) % 2 == 0) {
                    square.setBackground(Color.WHITE);
                } else {
                    square.setBackground(Color.DARK_GRAY);
                }
                chessBoard.add(square);
            }
        }
        setVisible(true);

        /*
         * - Khởi tạo các quân cờ
         * */
        chessPieces = Helper.initDefaultChessMan();

        displayChessPieces();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component component = e.getComponent();
                Point point = e.getPoint();

                int squareSize = component.getWidth() / 8; // Kích thước của một ô cờ
                int column = point.x / squareSize; // Tính toán cột từ tọa độ x
                int row = point.y / squareSize; // Tính toán hàng từ tọa độ y
                printLog(String.format("O co select - Row %d - Column %d", row, column));
                if (selectedPiece == null) {
                    // Chọn quân cờ nếu ô cờ không trống
                    selectedPiece = getChessPieceAt(row, column);
//                    if(!isCheckTurn()) return;
                    if (null == selectedPiece) {
                        showPopupNotify("Không có quân cờ được chọn");
                        printLog("Không có quân cờ nào được chọn");
                    } else {
                        printLog(String.format("Chon quan %s, mau = %s row = %d column = %d", selectedPiece.getType(), selectedPiece.getColor() == Color.BLACK ? "BLACK" : "WHITE", selectedPiece.getRow(), selectedPiece.getColumn()));
                    }
                } else {
                    ChessPiece chessSelectHistory = selectedPiece.cloneChess();
                    // Di chuyển quân cờ nếu ô cờ trống và hợp lệ
                    if (isValidMove(selectedPiece, row, column)) {
                        ChessPiece isChessExist = getChessPieceAt(row, column);
                        for (ChessPiece piece : chessPieces) {
                            if (piece.getRow() == selectedPiece.getRow() && piece.getColumn() == selectedPiece.getColumn()) {
                                /*
                                - Xóa iconimage quân cờ ở jpanel cũ đi
                                - Bổ sung iconimage cho jpanel mới
                                - Kiểm tra xem ô đó có tồn tại quân cờ khác màu hay chưa nếu tồn tại => xóa
                                */
                                if (isChessExist != null) {
                                    if (isChessExist.getColor() == piece.getColor()) {
                                        //Giống màu cùng 1 phe không thể ăn lẫn nhau
                                        printLog("Khong the an quan co cung 1 phe!");
                                        showPopupNotify("Không thể ăn quân cờ cùng phe!");
                                        break;
                                    }
                                }
                                JPanel square = (JPanel) chessBoard.getComponent(piece.getRow() * 8 + piece.getColumn());
                                square.removeAll();
                                piece.setRow(row);
                                piece.setColumn(column);
                                piece.setColor(selectedPiece.getColor());
                                JPanel squareNew = (JPanel) chessBoard.getComponent(piece.getRow() * 8 + piece.getColumn());
                                JLabel pieceLabel = new JLabel(getChessPieceIcon(selectedPiece.getType(), selectedPiece.getColor()));
                                if (isChessExist != null) {
                                    squareNew.removeAll();
                                }
                                squareNew.add(pieceLabel);
                                break;
                            }
                        }
                        isTurn = !isTurn;

                        ChessCheckmate chessCheckmate = CheckmateService.checkmateKing();
                        if (chessCheckmate != null) {
                            /*
                             * - Kiểm tra cờ chết
                             * */
                            if (checkChessDie(chessCheckmate)) {
                                /*
                                 * - Trong trường hợp xảy ra chiếu cờ và quân vua không còn nước cờ nào có thể đi.
                                 * - Thì kiểm tra xem phía quân bị chiếu có quân nào ăn được quân chiếu hay không
                                 * */
                                if(!CheckmateService.killCheckmateKing(chessCheckmate.getChessCheckmate())){
                                    /*
                                    * - Nếu không quân cờ nào có thể kill được quân cờ chiếu vua,
                                    *       kiểm tra xem có quân cờ nào có thể ra chặn chiếu quân vua được không
                                    *       trong trường hợp quân chiếu là tốt mã hoặc townsg đối phương thì không cần phải check case này
                                    * */
                                    if(!CheckmateService.shieldChessKing(chessCheckmate.getChessCheckmate() ,chessCheckmate.getKing())){
                                        showPopupNotify(String.format("Hết cờ, phía cờ %s thắng", selectedPiece.getColor() == Color.WHITE ? "Trắng" : "Đen"));
                                        System.exit(1);
                                    }
                                }

                            } else {
                                printLog("Quân vua vẫn còn đường để đi");
                            }

                            /*
                             * - Sau khi di chuyển quân cờ kiểm tra xem quân VUA của người chơi vừa di chuyển có bị chiếu hay không
                             * - Nếu có thực hiện show popup warning và thực hiện reset nước cờ vừa đi
                             * */
                            if (chessCheckmate.getKing().getColor() == selectedPiece.getColor()) {
                                showPopupNotify(String.format("Quân vua của bạn đang gặp nguy hiểm bởi quân %s của đối thủ !!", chessCheckmate.getChessCheckmate().getType()));
                                /*
                                 * - Khôi phục lại quân cờ nếu bị đối thủ ăn mất
                                 * */
                                JPanel squareRefresh = (JPanel) chessBoard.getComponent(row * 8 + column);
                                squareRefresh.removeAll();
                                if (isChessExist != null) {
                                    JLabel pieceLabel = new JLabel(getChessPieceIcon(isChessExist.getType(), isChessExist.getColor()));
                                    squareRefresh.add(pieceLabel);
                                }

                                /*
                                 * - reset lại quân cờ vừa đánh
                                 * */
                                for (ChessPiece piece : chessPieces) {
                                    if (piece.getRow() == selectedPiece.getRow() && piece.getColumn() == selectedPiece.getColumn()) {
                                        piece.setColumn(chessSelectHistory.getColumn());
                                        piece.setRow(chessSelectHistory.getRow());
                                        JPanel square = (JPanel) chessBoard.getComponent(chessSelectHistory.getRow() * 8 + chessSelectHistory.getColumn());
                                        JLabel pieceLabel = new JLabel(getChessPieceIcon(chessSelectHistory.getType(), chessSelectHistory.getColor()));
                                        square.add(pieceLabel);
                                    }
                                }
                            }

                            /*
                             * - Xóa quân cờ nếu bị ăn bởi đối thủ
                             * */
                        }
                        if (isChessExist != null) chessPieces.remove(isChessExist);
                    } else {
                        printLog(String.format("Nuoc di khong hop le cho quan %s", selectedPiece.getType()));
                        showPopupNotify("Nước cờ đi không hợp lệ");
                    }
                    selectedPiece = null; // Bỏ chọn quân cờ
                    revalidate();
                    repaint();
                }
            }
        });
    }

    /*
     * - Dùng để kiểm tra lượt đi xen kẽ đen và trắng
     * */
    private boolean isCheckTurn() {
        if (isTurn && selectedPiece.getColor() == Color.BLACK) {
            showPopupNotify("Lượt đi của quân trắng!");
            selectedPiece = null;
            return false;
        } else if (!isTurn && selectedPiece.getColor() == Color.WHITE) {
            showPopupNotify("Lượt đi của quân đen!");
            selectedPiece = null;
            return false;
        } else {
            return true;
        }
    }

    /*
        - Trả về true & false
        - True là nước đi hợp lệ được phép
        - False là nước đi không hợp lệ
        - Sử dụng switch case chia trường hợp các quân cờ có cách đi khác nhau: tốt, xe , mã, tượng, hậu, tướng
    */
    private boolean isValidMove(ChessPiece piece, int row, int column) {
        boolean result = false;
        switch (piece.getType()) {
            case "MA" -> result = StepService.isValidQuanMa(piece.getRow(), piece.getColumn(), row, column);
            case "TOT" -> {
                boolean isFirstMove;
                if (piece.getColor() == Color.WHITE) {
                    isFirstMove = piece.getRow() == 1;
                } else {
                    isFirstMove = piece.getRow() == 6;
                }
                result = isValidQuanTot(piece.getRow(), piece.getColumn(), row, column, isFirstMove, piece.getColor() == Color.BLACK);
            }
            case "XE" -> result = StepService.isValidQuanXe(piece.getRow(), piece.getColumn(), row, column);
            case "TUONG" -> result = StepService.isValidTuong(piece.getRow(), piece.getColumn(), row, column);
            case "HAU" -> result = StepService.validHau(piece.getRow(), piece.getColumn(), row, column);
            case "VUA" -> result = StepService.validVua(piece.getRow(), piece.getColumn(), row, column);
        }
        return result;
    }


    private void displayChessPieces() {
        for (ChessPiece piece : chessPieces) {
            JPanel square = (JPanel) chessBoard.getComponent(piece.getRow() * 8 + piece.getColumn());
            // Tạo một đối tượng JLabel để hiển thị quân cờ
            JLabel pieceLabel = new JLabel(getChessPieceIcon(piece.getType(), piece.getColor()));
            square.add(pieceLabel);
        }
    }

    private ImageIcon getChessPieceIcon(String type, Color color) {
        String image = Helper.PATH_IMAGE + String.format("%s_%s.gif", type, color == Color.WHITE ? "TRANG" : "DEN");
        return new ImageIcon(image);
    }

    /*
    * - Xử lý thăng cấp đối với quân tốt khi di chuyển đến cuối bàn cờ
    * - Hiển thị popup cho phép người chơi chọn giữa quân xe, tượng, mã, hậu
    * */
    public static void leverUp(boolean isBlack, int row, int column) {
        String formatImage = Helper.PATH_IMAGE + "%s_%s.gif";
        ImageIcon hau = new ImageIcon(String.format(formatImage, "HAU", isBlack ? "DEN" : "TRANG"));
        ImageIcon xe = new ImageIcon(String.format(formatImage, "XE", isBlack ? "DEN" : "TRANG"));
        ImageIcon ma = new ImageIcon(String.format(formatImage, "MA", isBlack ? "DEN" : "TRANG"));
        ImageIcon tuong = new ImageIcon(String.format(formatImage, "TUONG", isBlack ? "DEN" : "TRANG"));

        JButton buttonHau = new JButton(hau);
        JButton buttonXe = new JButton(xe);
        JButton buttonMa = new JButton(ma);
        JButton buttonTuong = new JButton(tuong);

        JPanel square = (JPanel) chessBoard.getComponent(row * 8 + column);
        JOptionPane.getRootFrame().dispose();

        buttonHau.addActionListener(e -> {
            chessPieces.remove(selectedPiece);
            selectedPiece = new ChessPiece("HAU", isBlack ? Color.BLACK : Color.WHITE, row, column);
            chessPieces.add(new ChessPiece("HAU", isBlack ? Color.BLACK : Color.WHITE, row, column));
            square.removeAll();
            JLabel pieceLabel = new JLabel(hau);
            square.add(pieceLabel);
            square.repaint();
            JOptionPane.getRootFrame().dispose();
        });

        buttonXe.addActionListener(e -> {
            chessPieces.remove(selectedPiece);
            selectedPiece = new ChessPiece("XE", isBlack ? Color.BLACK : Color.WHITE, row, column);
            chessPieces.add(new ChessPiece("XE", isBlack ? Color.BLACK : Color.WHITE, row, column));
            square.removeAll();
            JLabel pieceLabel = new JLabel(xe);
            square.add(pieceLabel);
            JOptionPane.getRootFrame().dispose();
        });

        buttonMa.addActionListener(e -> {
            chessPieces.remove(selectedPiece);
            selectedPiece = new ChessPiece("MA", isBlack ? Color.BLACK : Color.WHITE, row, column);
            chessPieces.add(new ChessPiece("MA", isBlack ? Color.BLACK : Color.WHITE, row, column));
            square.removeAll();
            JLabel pieceLabel = new JLabel(ma);
            square.add(pieceLabel);
            JOptionPane.getRootFrame().dispose();
        });

        buttonTuong.addActionListener(e -> {
            chessPieces.remove(selectedPiece);
            selectedPiece = new ChessPiece("TUONG", isBlack ? Color.BLACK : Color.WHITE, row, column);
            chessPieces.add(new ChessPiece("TUONG", isBlack ? Color.BLACK : Color.WHITE, row, column));
            square.removeAll();
            JLabel pieceLabel = new JLabel(tuong);
            square.add(pieceLabel);
            JOptionPane.getRootFrame().dispose();
        });

        // Mảng chứa các nút lựa chọn và biểu tượng tương ứng
        Object[] options = {buttonHau, buttonXe, buttonMa, buttonTuong};

        // Hiển thị cửa sổ popup với nút lựa chọn và biểu tượng
//        int result = JOptionPane.showOptionDialog(null, "Chọn một tùy chọn", "Thăng cấp",
//                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        // Xử lý kết quả từ nút được chọn

        showPopupMultipleOption("Chọn một tùy chọn", "Thăng cấp", options);
    }

    public boolean isValidQuanTot(int startX, int startY, int endX, int endY, boolean isFirstMove, boolean isBlackPiece) {
        // Trường hợp thăng cấp của Quân Tốt
        // Kiểm tra nếu Quân Tốt đã di chuyển đến hàng bên trên cùng hoặc hàng dưới cùng của bàn cờ
        if (endX == 0 || endX == 7) {
            // Thực hiện xử lý thăng cấp
            leverUp(isBlackPiece, startX, startY);
        }
        return StepService.isValidQuanTot(startX, startY, endX, endY, isFirstMove, isBlackPiece);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGame::new);
    }
}
