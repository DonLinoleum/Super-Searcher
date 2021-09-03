package sample;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.io.File;
import javafx.application.Platform;



public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField filename;

    @FXML
    private TextField path;

    //кнопка поиска
    @FXML
    private Button search;

    //Label в нижней части, с результатами
    @FXML
    private Label result;

    //Label в нижней части, информируюущая о процессе поиска
    @FXML
    private Label resLabel;

    //функция поиска
    public void Search(File fileName, File dir)
    {
        Task task = new Task<Void>()
        {
            @Override
            public Void call()
            {
                //функция runLater, поток в javaFX, реализующая интерфейс Runnable.
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //Создаем массив всех файлов и директорий по указанному пути
                            File[] list = dir.listFiles();
                            if (list != null)
                            {
                                //Проходим по этому массиву
                                for (File item : list)
                                {
                                    //Если директория, то сравниваем имя.
                                    if (item.isDirectory())
                                    {
                                        if (fileName.getName().equalsIgnoreCase(item.getName()) ||
                                                (item.getName().toLowerCase()).startsWith(fileName.getName().toLowerCase()))
                                        {
                                            result.setText(result.getText() +"\n" + item.getParentFile() + "\\" + item.getName());
                                            resLabel.setText("Результаты: ");
                                        }
                                        Search(fileName, item);
                                    }
                                    //Если файл, то сравниваем имя
                                    else if (fileName.getName().equalsIgnoreCase(item.getName()))
                                    {
                                        result.setText(result.getText() +"\n" + item.getParentFile() + "\\" + item.getName());
                                        resLabel.setText("Результаты: ");
                                    }
                                }
                            }
                            //Если указанный путь не существует, выводим об этом сообщение
                            else if (list == null)
                            {
                                result.setText("Директория не найдена!");
                            }
                    }
                });
                // функция public Void call() должна что то возвращать. возвращаем.
                return null;
            }
        };
        //запускаем поток с функции Platform.runLater.
        new Thread(task).start();
    }

    @FXML
    void initialize()
    {
        //устанавливаем обработчик нажатия на кнопку поиска
        search.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                resLabel.setText("Поиск: ");
                result.setText("Ищем...");

                File dir = new File(path.getText().trim());
                File fileName = new File (filename.getText().trim());

                //При нажатии кнопки поиска запускаем функцию поиска (строка 44)
                Search(fileName,dir);
            }
        });

    }
}

