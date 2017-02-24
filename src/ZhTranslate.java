import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created by hyhe on 17-2-24.
 */
public class ZhTranslate extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ZhTranslateDialog dialog = new ZhTranslateDialog();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
