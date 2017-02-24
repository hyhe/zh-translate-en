import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ZhTranslateDialog extends JDialog {
    private JPanel contentPane;
    private JTextField textField1;
    private JScrollPane scrollPane;
    private JTextArea textArea1;
    private JButton buttonOK;

    boolean isTranslateFinished = false;

    public ZhTranslateDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        hideTextArea();

        textField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                isTranslateFinished = false;
                hideTextArea();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                isTranslateFinished = false;
                hideTextArea();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                isTranslateFinished = false;
                hideTextArea();

            }
        });
        //第一次enter进行翻译，第二次enter关闭面板
        textField1.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isTranslateFinished) {
                    dispose();
                } else {
                    String text = textField1.getText();
                    if (text != null && text.length() > 0) {
                        doTranslate(text);
                    } else {
                        dispose();
                    }
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * 使用有道 open api翻译
     */
    public void doTranslate(String text) {
        try {
            String url = "http://fanyi.youdao.com/openapi.do";
            String params = "keyfrom=firebirdfly&key=2125217244&type=data&doctype=json&version=1.1&q=" + URLEncoder.encode(text, "utf-8");
            String ret = HttpRequest.doRequest(url, params);
            Gson gson = new Gson();
            RequestRet objectRet = gson.fromJson(ret, RequestRet.class);
            showTranslateRet(objectRet);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    void hideTextArea() {
        scrollPane.setVisible(false);
        textArea1.setText("");
        textArea1.setVisible(false);
        this.setSize(400, 115);
    }

    void showTextArea() {
        scrollPane.setVisible(true);
        textArea1.setVisible(true);
        this.setSize(400, 250);
    }

    /**
     * 显示翻译结果
     */
    public void showTranslateRet(RequestRet obj) {
        if(obj.getErrorCode() == 0) {
            if (obj.getTranslation() != null && obj.getTranslation().size() > 0) {
                for (String str : obj.getTranslation()) {
                    textArea1.append(str + "\n\r");
                }
            } else {
                showErrorMsg();
            }
        }else{
            showErrorMsg();
        }
        showTextArea();
        isTranslateFinished = true;
    }

    void showErrorMsg(){
        textArea1.setText("未查询到！");
    }

}
