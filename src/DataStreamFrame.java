import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;

public class DataStreamFrame extends JFrame
{
    //Declarations

    //Panels
    JPanel mainPnl;
    JPanel buttonPnl;
    JPanel searchPnl;
    JPanel textPnl;
    JPanel filePnl;
    JPanel returnPnl;

    //JButtons
    JButton quitBtn;
    JButton searchBtn;
    JButton fileBtn;

    //JScrollPane
    JScrollPane fileScroller;
    JScrollPane returnScroller;

    //JTextField
    JTextField searchField;

    //JTextArea
    JTextArea fileArea;
    JTextArea returnArea;

    //Jlabels
    JLabel searchLabel;

    //other declarations
    JFileChooser chooser = new JFileChooser();
    File selectedFile;
    String line = "";
    Map<String, List<String>> searchWord = new TreeMap<>();

    public DataStreamFrame()
    {
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());



        createSearchPnl();
        mainPnl.add(searchPnl, BorderLayout.NORTH);

        createTextPnl();
        mainPnl.add(textPnl, BorderLayout.CENTER);

        createButtonPnl();
        mainPnl.add(buttonPnl, BorderLayout.SOUTH);



        add(mainPnl);
        setTitle("Data Stream");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createTextPnl()
    {
        textPnl = new JPanel();
        textPnl.setLayout(new BorderLayout());

        createFilePnl();
        textPnl.add(filePnl, BorderLayout.LINE_START);

        createReturnPnl();
        textPnl.add(returnPnl, BorderLayout.LINE_END);
    }

    private void createSearchPnl()
    {
        searchPnl = new JPanel();
        searchPnl.setLayout(new GridLayout(1,3));

        searchLabel = new JLabel("Search Here: ");
        searchField = new JTextField();
        searchBtn = new JButton("Search");

        searchBtn.addActionListener((ActionEvent ae) -> getReturn3());

        searchPnl.add(searchLabel);
        searchPnl.add(searchField);
        searchPnl.add(searchBtn);

    }


    private void createFilePnl()
    {
        filePnl = new JPanel();

        fileArea = new JTextArea(20,30);
        fileArea.setEditable(false);

        fileScroller = new JScrollPane(fileArea);
        filePnl.add(fileScroller);

    }

    private void createReturnPnl()
    {
        returnPnl = new JPanel();

        returnArea = new JTextArea(20,30);
        returnArea.setEditable(false);

        returnScroller = new JScrollPane(returnArea);
        returnPnl.add(returnScroller);
    }


    private void createButtonPnl() //makes the button panel
    {
        buttonPnl= new JPanel();
        buttonPnl.setLayout(new GridLayout(1, 2));

        fileBtn = new JButton("Choose File");
        quitBtn = new JButton("Quit");

        fileBtn.addActionListener((ActionEvent ae) -> getFile());
        quitBtn.addActionListener((ActionEvent ae) -> System.exit(0));

        buttonPnl.add(fileBtn);
        buttonPnl.add(quitBtn);
    }

    private void getFile()
    {
        try
        {
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = chooser.getSelectedFile();

                Path file = selectedFile.toPath();

                fileArea.append("File name selected: " + selectedFile.getName());

                InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                line = reader.readLine();

                while (line != null)
                {
                    fileArea.append("\n" + line);
                    line = reader.readLine();
                }
            }
        }
        catch (FileNotFoundException e)
        {
            fileArea.append("File not found.");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void getReturn3()
    {
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            selectedFile = chooser.getSelectedFile();
            Path file2 = selectedFile.toPath();


            try(Stream<String> lines = Files.lines(file2))
            {
                lines.forEach(line ->
                        {
                            String processedLine = line.toLowerCase();

                            if (processedLine.contains(searchField.getText().toLowerCase()))
                            {
                                returnArea.append(line + "\n");
                            }

                        }
                );
            }
            catch (FileNotFoundException e)
            {
                fileArea.append("File not found.");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }

    }

}
