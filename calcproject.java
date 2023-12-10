// Import statements for JFreeChart, Swing components, Scanner, and function handling
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.License;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.util.function.Function;

// Main class for the numerical integration calculator, extending ApplicationFrame
public class calcproject extends ApplicationFrame {

    // Constants for default and minimum font size
    private static final int DEFAULT_FONT_SIZE = 12;
    private static final int MIN_FONT_SIZE = 8;

    // Constructor for the main class
    public calcproject(String title) {
        super(title);
    }

    // Method to create an adjusted font based on label text and panel width
    private static Font createAdjustedFont(Font baseFont, int targetWidth, String labelText, JPanel labelPanel) {
        FontMetrics metrics = labelPanel.getFontMetrics(baseFont);
        int width = metrics.stringWidth(labelText);
        int fontSize = baseFont.getSize();

        // Adjust font size until the label fits within the target width
        while (width > targetWidth && fontSize > MIN_FONT_SIZE) {
            fontSize--;
            baseFont = new Font(baseFont.getName(), baseFont.getStyle(), fontSize);
            metrics = labelPanel.getFontMetrics(baseFont);
            width = metrics.stringWidth(labelText);
        }

        return baseFont;
    }

    // Method to update label font based on the current panel width
    private static void updateLabelFont(JPanel labelPanel, String labelText) {
        Font baseFont = labelPanel.getFont();
        int targetWidth = labelPanel.getWidth() / 5; // Divide by the number of labels
        Font adjustedFont = createAdjustedFont(baseFont, targetWidth, labelText, labelPanel);

        labelPanel.setFont(adjustedFont);
    }

    // Method to create a JFreeChart for a given function and interval
    public JFreeChart createChart(
            Function<Double, Double> f,
            double a,
            double b,
            int n) {

        // Create a series for the function data
        XYSeries functionSeries = new XYSeries("Function");

        double h = (b - a) / n;
        double x = a;
        for (int i = 0; i <= n; i++) {
            double y = f.apply(x);
            functionSeries.add(x, y);
            x += h;
        }

        // Create a dataset and chart using JFreeChart
        XYSeriesCollection dataset = new XYSeriesCollection(functionSeries);

        JFreeChart chart = ChartFactory.createXYAreaChart(
                "Function Plot",
                "x",
                "f(x)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize the appearance of the chart
        XYPlot plot = chart.getXYPlot();
        XYAreaRenderer renderer = new XYAreaRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, new Color(0, 255, 0, 128));
        plot.setRenderer(renderer);
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        return chart;
    }

    // Method for midpoint approximation of definite integral
    public static double midpointApproximation(Function<Double, Double> f, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0;
        for (int i = 0; i < n; i++) {
            double x = a + (i + 0.5) * h;
            sum += h * f.apply(x);
        }
        return sum;
    }

    // Method for left sum approximation of definite integral
    public static double leftSumApproximation(Function<Double, Double> f, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0;
        for (int i = 0; i < n; i++) {
            double x0 = a + i * h;
            double x1 = a + (i + 1) * h;
            sum += f.apply(x0) * (x1 - x0);
        }
        return sum;
    }

    // Method for right sum approximation of definite integral
    public static double rightSumApproximation(Function<Double, Double> f, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0;
        for (int i = 0; i < n; i++) {
            double x0 = a + i * h;
            double x1 = a + (i + 1) * h;
            sum += f.apply(x1) * (x1 - x0);
        }
        return sum;
    }

    // Method for trapezoid approximation of definite integral
    public static double trapezoidApproximation(Function<Double, Double> f, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = (f.apply(a) + f.apply(b)) / 2;
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += f.apply(x);
        }
        return h * sum;
    }

    // Method for Simpson's rule approximation of definite integral
    public static double simpsonApproximation(Function<Double, Double> f, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = f.apply(a) + f.apply(b);
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += (i % 2 == 0) ? 2 * f.apply(x) : 4 * f.apply(x);
        }
        return h * sum / 3;
    }

    // Main method for the numerical integration calculator
    public static void main(String[] args) {
        // Confirm non-commercial use
        License.iConfirmNonCommercialUse("JagodaOP");

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Welcome message and user input for the function, limits, and intervals
        System.out.print("Welcome to the numerical integration calculator. Please enter the function, limits of integration, and the number of intervals for graph and results.");
        System.out.println();
        System.out.print("Enter the function f(x): ");
        String functionStr = scanner.nextLine();

        // Convert the function string to a function using mxparser
        Function<Double, Double> f = x -> {
            Expression expression = new Expression(functionStr, new Argument("x", x));
            return expression.calculate();
        };

        System.out.print("Enter the lower limit of integration (a): ");
        double a = scanner.nextDouble();

        System.out.print("Enter the upper limit of integration (b): ");
        double b = scanner.nextDouble();

        System.out.print("Enter the number of subintervals (n): ");
        int n = scanner.nextInt();
        System.out.println();

        // Perform numerical approximations
        double midpointResult = midpointApproximation(f, a, b, n);
        double leftSumResult = leftSumApproximation(f, a, b, n);
        double rightSumResult = rightSumApproximation(f, a, b, n);
        double trapezoidResult = trapezoidApproximation(f, a, b, n);
        double simpsonResult = simpsonApproximation(f, a, b, n);

        // Create the chart and display it along with numerical results
        calcproject chartApp = new calcproject("Function Plot");
        JFreeChart chart = chartApp.createChart(f, a, b, n);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Create a panel for labels
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(0, 1)); // Single column layout

        // Add styled labels with border and font adjustments
        addStyledLabel(labelPanel, "Numerical Integration Calculations:");
        addStyledLabel(labelPanel, "Midpoint Approximation: " + midpointResult);
        addStyledLabel(labelPanel, "Left Sum Approximation: " + leftSumResult);
        addStyledLabel(labelPanel, "Right Sum Approximation: " + rightSumResult);
        addStyledLabel(labelPanel, "Trapezoid Approximation: " + trapezoidResult);
        addStyledLabel(labelPanel, "Simpson Approximation: " + simpsonResult);

        // Create a frame to hold both chart and labels
        JFrame frame = new JFrame("Function Plot");
        frame.setLayout(new BorderLayout());
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.add(labelPanel, BorderLayout.WEST); // Change layout position to the left

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);

        // Close the scanner
        scanner.close();
    }

    // Method to facilitate label creation
    private static void addLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
    }

    // Method to facilitate styled label creation
    private static void addStyledLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);

        // Add styles
        if (text.equals("Numerical Integration Calculations:")) {
            label.setFont(label.getFont().deriveFont(Font.ITALIC | Font.BOLD));
        } else {
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
        }

        // Set label alignment, add border, and set padding
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2), // Border color and thickness
                BorderFactory.createEmptyBorder(5, 10, 5, 10)  // Add padding inside the border
        ));
        panel.add(label);
    }
}
