import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Stack;
import java.util.*;


public class Calculator extends JFrame {
    JButton[] digits =
            {
                    new JButton(("0")),
                    new JButton(("1")),
                    new JButton(("2")),
                    new JButton(("3")),
                    new JButton(("4")),
                    new JButton(("5")),
                    new JButton(("6")),
                    new JButton(("7")),
                    new JButton(("8")),
                    new JButton(("9"))
            };
    JButton[] operators =
            {
                    new JButton(("+")),
                    new JButton(("-")),
                    new JButton(("*")),
                    new JButton(("/")),
                    new JButton(("=")),
                    new JButton(("(")),
                    new JButton((")")),
                    new JButton(("C"))
            };
    String[] oper_values = {"+", "-", "*", "/", "=", "(", ")", ""};
    char operator;
    JTextArea area = new JTextArea(3,5);
    public static void main(String[] args)
    {
        Calculator calculator = new Calculator();
        calculator.setSize(260,210);
        calculator.setTitle("Java-clac, PP Lab1");
        calculator.setResizable(false);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calculator()
    {
        add(new JScrollPane(area), BorderLayout.NORTH);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());

        for(int i=0; i<10; i++)
            buttonpanel.add(digits[i]);
        for(int i=0; i<8; i++)
            buttonpanel.add(operators[i]);

        add(buttonpanel, BorderLayout.CENTER);
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        for(int i=0; i<10; i++)
        {
            int finalI = i;
            digits[i].addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent actionEvent)
                {
                    area.append(Integer.toString(finalI));
                }
            });
        }
        for(int i=0; i<8; i++)
        {
            int finalI = i;
            operators[i].addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent actionEvent)
                {
                    Stack <Double> operanzi = new Stack<>();
                    Stack <Character> operatori = new Stack<>();
                    if(finalI == 7)
                        area.setText("");
                    else
                    if(finalI == 4)
                    {

                        try
                        {
                            List<String> postfix= infixToPostfix(area.getText());
                            double rez = evaluare(postfix);
                            area.setText( rez + "");
                        }
                        catch(Exception e)
                        {
                            area.setText(" !!! Probleme de ImPlEmEnTaRe !!!");
                        }
                    }
                    else
                    {
                        area.append(oper_values[finalI]);
                        operator = oper_values[finalI].charAt(0);
                    }
                }
            });

        }
    }

    public static List<String> infixToPostfix(String expresie)
    {
        List<String> postfix = new ArrayList<>();
        Stack<Character> opStack = new Stack<>();

        int i = 0;
        while (i < expresie.length())
        {
            char c = expresie.charAt(i);

            if (Character.isDigit(c))
            {
                int j = i;
                while (j < expresie.length() && Character.isDigit(expresie.charAt(j)))
                {
                    j++;
                }
                postfix.add(expresie.substring(i,j));
                i=j;
                continue;
            }

            if (c == '(') {
                opStack.push(c);
            } else if (c == ')')
            {
                while (!opStack.isEmpty() && opStack.peek() != '(')
                {
                    postfix.add(String.valueOf(opStack.pop()));
                }
                opStack.pop();
            } else
            {
                while (!opStack.isEmpty() && prioritate(opStack.peek()) >= prioritate(c))
                {
                    postfix.add(String.valueOf(opStack.pop()));
                }
                opStack.push(c);
            }
            i++;
        }

        while (!opStack.isEmpty())
            postfix.add(String.valueOf(opStack.pop()));

        return postfix;
    }

    public static double evaluare(List<String> postfix)
    {
        Stack<Double> numStack = new Stack<>();
        while(!postfix.isEmpty())
        {
            if (postfix.get(0).chars().allMatch(Character::isDigit))
            {
                numStack.push(Double.parseDouble(postfix.get(0)));
                postfix.remove(0);
            }
            else
            {
                double b = numStack.pop();
                double a = numStack.pop();
                char operatie=postfix.get(0).charAt(0);
                switch(operatie)
                {
                    case '+' : numStack.push(a+b); break;
                    case '-' : numStack.push(a-b); break;
                    case '*' : numStack.push(a*b); break;
                    case '/' : numStack.push(a/b); break;
                    default: numStack.push(0.0);
                }

                postfix.remove(0);
            }
        }
        return numStack.pop();
    }

    private static int prioritate(char op)
    {
        if(op == '*' || op == '/')
            return 2;
        else
            if(op == '+' || op == '-')
                return 1;
            else
                return 0;
    }
}