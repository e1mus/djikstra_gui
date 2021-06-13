package gui;

import models.Edge;
import models.Graph;
import models.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import algo.oneNumber;
import java.util.List;


public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener {

    private DrawUtils drawUtils;

    private Graph graph;

    private Node selectedNode = null;
    private Node hoveredNode = null;
    private Edge hoveredEdge = null;

    private java.util.List<Node> path = null;
    private oneNumber numbers;
    private Point cursor;

    public GraphPanel(Graph graph){
        this.graph = graph;

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setPath(List<Node> path) {
        this.path = path;
        hoveredEdge = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawUtils = new DrawUtils(graphics2d);

        if(graph.isSolved()){
            drawUtils.drawPath(path);
        }

        if(selectedNode != null && cursor != null){
            Edge e = new Edge(selectedNode, new Node(cursor));
            drawUtils.drawEdge(e);
        }

        for(Edge edge : graph.getEdges()){
            if(edge == hoveredEdge)
                drawUtils.drawHoveredEdge(edge);
            drawUtils.drawEdge(edge);
        }

        for(Node node : graph.getNodes()){
            if(node == selectedNode || node == hoveredNode)
                drawUtils.drawHalo(node);
            if(graph.isSource(node))
                drawUtils.drawSourceNode(node);
            else if(graph.isDestination(node))
                drawUtils.drawDestinationNode(node);
            else
                drawUtils.drawNode(node);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        Node selected = null;
        for(Node node : graph.getNodes()) {
            if(DrawUtils.isWithinBounds(e, node.getCoord())){
                selected = node;
                break;
            }
        }

        if(selected!=null) {
            if(e.isControlDown() && e.isShiftDown()){
                graph.deleteNode(selected);
                graph.setSolved(false);
                repaint();
                return;
            } else if(e.isControlDown() && graph.isSolved()){
                path = selected.getPath();
                repaint();
                return;
            } else if(e.isShiftDown()){
                if(SwingUtilities.isLeftMouseButton(e)){
                    if(!graph.isDestination(selected))
                        graph.setSource(selected);
                    else
                        JOptionPane.showMessageDialog(null, "Belgilangan joy Manba sifatida belgilanmaydi");
                } else if(SwingUtilities.isRightMouseButton(e)) {
                    if(!graph.isSource(selected))
                        graph.setDestination(selected);
                    else
                        JOptionPane.showMessageDialog(null, "Grafda oxirgi manzil sifatida o'rnatilamagan ");
                }else
                    return;

                graph.setSolved(false);
                repaint();
                return;
            }
        }

        if(hoveredEdge!=null){
            if(e.isControlDown() && e.isShiftDown()){
                graph.getEdges().remove(hoveredEdge);
                hoveredEdge = null;
                graph.setSolved(false);
                repaint();
                return;
            }

            String input = JOptionPane.showInputDialog("Uchi uchun o'g'irlik(agar bir necha parametr bo'lsa probel bilan yozing) " + hoveredEdge.toString()
                                                        + " : ");
            try {
                if(input.split("\\D+").length ==1)
                {
                    int weight = Integer.parseInt(input);
                    if (weight > 0) {

                        hoveredEdge.setWeight(weight);
                        graph.setSolved(false);
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Og'irligi musbar bo'lishi kerak");
                    }
                }else {
                    numbers = new oneNumber();
                    String[] data = input.split("\\D+");
for (int i=0 ; i< data.length; i++){
    System.out.println(data[i]);
}
                    if (data.length >= 5 && (data.length - 1) / 2 > Integer.parseInt(data[data.length - 1])) {
                        double[] a = new double[(data.length - 1) / 2];
                        for (int i = 0; i < (data.length-1)/2; i++) {
                            a[i] = Double.parseDouble(data[i]);
                            System.out.print(a[i]+" ");
                        }
                        System.out.println();
                        int k=0;
                        double[] b = new double[(data.length - 1) / 2];
                        for (int i = (data.length-1)/2; i < data.length-1; i++) {
                            System.out.print(data[i]+" ");
                            b[k++] = Double.parseDouble(data[i]);

                        }
                        System.out.println();
                        int l = Integer.parseInt(data[data.length - 1] );
                        System.out.print(l+" ");
                        int count = b.length;
                        hoveredEdge.setWeight(Integer.parseInt(numbers.myFull(a,b,l,count)));
                        graph.setSolved(false);
                        repaint();
                    }else{
                        JOptionPane.showMessageDialog(null, "Parametrlar noto'g'ri kiritilgan");
                    }
                }

            } catch (NumberFormatException nfe) {}
            return;
        }

        for(Node node : graph.getNodes()) {
            if(DrawUtils.isOverlapping(e, node.getCoord())){
                JOptionPane.showMessageDialog(null, "Qatlamli tugun yaratib bo'lmaydi");
                return;
            }
        }

        graph.addNode(e.getPoint());
        graph.setSolved(false);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Node node : graph.getNodes()) {
            if(selectedNode !=null && node!= selectedNode && DrawUtils.isWithinBounds(e, node.getCoord())){
                Edge new_edge = new Edge(selectedNode, node);
                graph.addEdge(new_edge);
                graph.setSolved(false);
            }
        }
        selectedNode = null;
        hoveredNode = null;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        hoveredNode = null;

        for (Node node : graph.getNodes()) {
            if(selectedNode ==null && DrawUtils.isWithinBounds(e, node.getCoord())){
                selectedNode = node;
            } else if(DrawUtils.isWithinBounds(e, node.getCoord())) {
                hoveredNode = node;
            }
        }

        if(selectedNode !=null){
            if(e.isControlDown()){
                selectedNode.setCoord(e.getX(), e.getY());
                cursor = null;
                repaint();
                return;
            }

            cursor = new Point(e.getX(), e.getY());
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if(e.isControlDown()){
            hoveredNode = null;
            for (Node node : graph.getNodes()) {
                if(DrawUtils.isWithinBounds(e, node.getCoord())) {
                    hoveredNode = node;
                }
            }
        }

        hoveredEdge = null;

        for (Edge edge : graph.getEdges()) {
            if(DrawUtils.isOnEdge(e, edge)) {
                hoveredEdge = edge;
            }
        }

        repaint();
    }

    public void reset(){
        graph.clear();
        selectedNode = null;
        hoveredNode = null;
        hoveredEdge = null;
        repaint();
    }
}
