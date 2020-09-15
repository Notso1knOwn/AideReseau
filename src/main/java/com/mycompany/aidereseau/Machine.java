/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aidereseau;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author a.masvidal
 */
public class Machine extends javax.swing.JPanel {

    private String masque;
    private String netId = "0";
    ArrayList<String> listIP = new ArrayList<String>();

    /**
     * Creates new form Machine
     */
    public Machine() {
        initComponents();
    }
    
    public String getNetId() {
        return netId;
    }
    
    /**
     * Cette méthode permet de calculer la classe de l'@IP
     *
     * @return La classe de l'adresse IP
     */
    public String classeDeLAdresseIP() {
        if (Integer.parseInt(jTextFieldIP1.getText()) < 128) {
            return "A";
        } else {
            return "B";
        }
    }

    /**
     * Initialise la valeur du JTextField à "" pour un soucis d'esthétique
     *
     * @param text
     */
    public void InitNumber(JTextField text) {
        if (text.getText().equals("0")) {
            text.setText("");
        }
    }

    /**
     * Verifie Si le champ est vide auquel cas il le remplit avec 0
     *
     * @param text
     */
    public void VerifNumber(JTextField text) {
        if (text.getText().equals("")) {
            text.setText("0");
        }
    }
    
    /**
     * Verifie si la valeur d'un champ JTextField est un int à la perte du focus de ce dernier.
     * @param textField 
     */
    public void VerifChamp(JTextField textField){
        try {
            Integer.parseInt(textField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(textField, "Veuillez rentrer une valeur de type numérique et entière");
            textField.setText("0");
        }
    }
    
    /**
     * Procedure qui permet l'affichage de toutes les ip dans le réseau ou sous réseau dans le lequel se trouve l'adresse ip.
     * Les adreeses réseau et de broadcast ne sont pas listé.
     * Calcul la position du sous réseau, le pas du Reseau
     * Decoupe le net id
     * @param positionSR
     * @param PasReseau
     * @param nbHote
     * @param netId 
     */
    public void ListAllIP(int nbBitHote, int nbHote, String netId) {
        // détermine la position de droite à gauche et de 0 à 3 de la partie sous-réseau ou réseau du masque de sous réseau
            int positionSR = (int) nbBitHote / 8;
            
        // Calcul du pas de réseau soit le nombre qui permet de passer d'un sous réseau à un autre
        int PasReseau = (nbBitHote % 8 != 0) ? (256 / (int) Math.pow(2, (8 - nbBitHote % 8))) : (1);
        
        // Découpage du net id pour la concaténation des chaînes de caractère dans le switch
        String[] splitNetId = netId.split("\\.");
        
        // En fonction de la position du sous réseau
        switch (positionSR) {
                case 3:
                    {
                        DefaultListModel leModel= (DefaultListModel)jList1.getModel();
                        for (int i = 0; i < PasReseau; i++) {
                            for (int j = 0; j < 256; j++) {
                                for (int k = 0; k < 256; k++) {
                                    for (int l = 0; l < 256; l++) {
                                        String temp = String.valueOf(Integer.parseInt(splitNetId[0]) + i);
                                        String tempAdd = temp + "." + String.valueOf(j) + "." + String.valueOf(k)+ "." +String.valueOf(l);
                                    
                                    leModel.addElement(tempAdd);
                                    }
                                }
                            }
                        }       
                        leModel.remove(0);
                        leModel.remove(nbHote);
                        break;
                    }
                case 2:
                    {
                        DefaultListModel leModel= (DefaultListModel)jList1.getModel();
                        for (int i = 0; i < PasReseau; i++) {
                            for (int j = 0; j < 256; j++) {
                                for (int k = 0; k < 256; k++) {
                                    
                                    String temp = String.valueOf(Integer.parseInt(splitNetId[1]) + i);
                                    String tempAdd = splitNetId[0] + "." + temp + "." + String.valueOf(j) + "." + String.valueOf(k);
                                    
                                    leModel.addElement(tempAdd);
                                    
                                }
                            }
                        }       
                        leModel.remove(0);
                        leModel.remove(nbHote);
                        break;
                    }
                case 1:
                    {
                        DefaultListModel leModel= (DefaultListModel)jList1.getModel();
                        for (int i = 0; i < PasReseau; i++) {
                            for (int k = 0; k < 256; k++) {
                                
                                String temp = String.valueOf(Integer.parseInt(splitNetId[2]) + i);
                                String tempAdd = splitNetId[0] + "." + splitNetId[1] + "." + temp + "." + String.valueOf(k);
                                
                                leModel.addElement(tempAdd);
                                
                            }
                        }       
                        leModel.remove(0);
                        leModel.remove(nbHote);
                        break;
                    }
                case 0:
                    for (int i = 1; i < PasReseau-1; i++) {
                        
                        String temp = String.valueOf(Integer.parseInt(splitNetId[3]) + i);
                        String tempAdd =splitNetId[0] + "." + splitNetId[1] + "." + splitNetId[2] + "." + temp;
                        
                        DefaultListModel leModel= (DefaultListModel)jList1.getModel(); 
                        leModel.addElement(tempAdd);
                        
                    }   
                    break;
                    
                default:
                    break;
            }
    }

    /**
     * Sert de procedure "maître", principale qui permet de remplir les labels de classe, de netId et de nombre d'hôte.
     * Appel la procedure ListAll(int nbBitHote, int nbHote, String netId).
     * Détermine la classe, le netId, le nombre de bit de la partie hôte et le nombre d'hote.
     * Regroupe dans un array les parties de l'IP donnée.
     * Découpe le masque.
     */
    public void CalculIP() {
        if (jTextFieldMasque.getText().equals("0")) {
            JOptionPane.showMessageDialog(jLabelClassText, "Veuillez rentrer un masque réseau");
        } else {
            this.masque = jTextFieldMasque.getText();

            String[] splitIP = {jTextFieldIP1.getText(), jTextFieldIP2.getText(), jTextFieldIP3.getText(), jTextFieldIP4.getText()};
            String[] splitMasque = jTextFieldMasque.getText().split("\\.");
            
            String netId = "";
            int nbBitHote = 0;

            for (int i = 0; i < 4; i++) {

                int tempIpPart = Integer.parseInt(splitIP[i]);
                int tempMasquePart = Integer.parseInt(splitMasque[i]);

                if (tempMasquePart != 255) {

                    // Détermination du nombre de bit pour la partie hôte
                    int number = (256 - tempMasquePart);
                    int incremente = 0;
                    while (number != 0) {
                        number = number / 2;
                        incremente++;
                    }
                    nbBitHote += incremente - 1;
                }
                
                // Comparaison binaire entre l'ip reçu et le masque de sous réseau
                String temp = String.valueOf(tempIpPart & tempMasquePart);
                netId += i != 0 ? "." + temp : temp;
                this.netId = netId;


            }
            
            // détermine le nombre d'hôte selon le nombre de bit de la partie hote
            double doubleNbHote = Math.pow(2, nbBitHote);
            int nbHote = (int) doubleNbHote - 2;

            // affecte les valeurs du netId et du nombre d'hôte aux champs alloués
            jLabelReseauxText.setText(netId);
            jLabelNbHoteText.setText(String.valueOf(nbHote));
            
            ListAllIP(nbBitHote, nbHote, netId);
            

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextFieldIP1 = new javax.swing.JTextField();
        jTextFieldIP2 = new javax.swing.JTextField();
        jTextFieldIP3 = new javax.swing.JTextField();
        jTextFieldIP4 = new javax.swing.JTextField();
        jTextFieldMasque = new javax.swing.JTextField();
        jLabelPoint1 = new javax.swing.JLabel();
        jLabelPoint2 = new javax.swing.JLabel();
        jLabelPoint3 = new javax.swing.JLabel();
        jButtonValider = new javax.swing.JButton();
        jTabbedPaneInfo = new javax.swing.JTabbedPane();
        jPanelClasse = new javax.swing.JPanel();
        jLabelClassText = new javax.swing.JLabel();
        jLabelClassLetter = new javax.swing.JLabel();
        jPanelAdresseR = new javax.swing.JPanel();
        jLabelReseau = new javax.swing.JLabel();
        jLabelReseauxText = new javax.swing.JLabel();
        jPanelNbHote = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabelNbHote = new javax.swing.JLabel();
        jLabelNbHoteText = new javax.swing.JLabel();

        jTextFieldIP1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldIP1.setText("0");
        jTextFieldIP1.setPreferredSize(new java.awt.Dimension(40, 40));
        jTextFieldIP1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldIP1FocusLost(evt);
            }
        });
        jTextFieldIP1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldIP1KeyPressed(evt);
            }
        });

        jTextFieldIP2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldIP2.setText("0");
        jTextFieldIP2.setPreferredSize(new java.awt.Dimension(40, 40));
        jTextFieldIP2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldIP2FocusLost(evt);
            }
        });
        jTextFieldIP2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldIP2KeyPressed(evt);
            }
        });

        jTextFieldIP3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldIP3.setText("0");
        jTextFieldIP3.setPreferredSize(new java.awt.Dimension(40, 40));
        jTextFieldIP3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldIP3FocusLost(evt);
            }
        });
        jTextFieldIP3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldIP3KeyPressed(evt);
            }
        });

        jTextFieldIP4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldIP4.setText("0");
        jTextFieldIP4.setPreferredSize(new java.awt.Dimension(40, 40));
        jTextFieldIP4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldIP4FocusLost(evt);
            }
        });
        jTextFieldIP4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldIP4KeyPressed(evt);
            }
        });

        jTextFieldMasque.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldMasque.setText("0");
        jTextFieldMasque.setPreferredSize(new java.awt.Dimension(30, 30));
        jTextFieldMasque.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldMasqueFocusLost(evt);
            }
        });
        jTextFieldMasque.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldMasqueKeyPressed(evt);
            }
        });

        jLabelPoint1.setText(".");

        jLabelPoint2.setText(".");

        jLabelPoint3.setText(".");

        jButtonValider.setText("Valider");
        jButtonValider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonValiderMouseClicked(evt);
            }
        });

        jLabelClassText.setText("Cette IP est de classe:");

        jLabelClassLetter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelClassLetter.setText("none");

        javax.swing.GroupLayout jPanelClasseLayout = new javax.swing.GroupLayout(jPanelClasse);
        jPanelClasse.setLayout(jPanelClasseLayout);
        jPanelClasseLayout.setHorizontalGroup(
            jPanelClasseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClasseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelClassText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelClassLetter, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelClasseLayout.setVerticalGroup(
            jPanelClasseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClasseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelClasseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelClassText)
                    .addComponent(jLabelClassLetter))
                .addContainerGap(170, Short.MAX_VALUE))
        );

        jTabbedPaneInfo.addTab("Classe", jPanelClasse);

        jLabelReseau.setText("L'adresse réseaux est:");

        jLabelReseauxText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelReseauxText.setText("none");

        javax.swing.GroupLayout jPanelAdresseRLayout = new javax.swing.GroupLayout(jPanelAdresseR);
        jPanelAdresseR.setLayout(jPanelAdresseRLayout);
        jPanelAdresseRLayout.setHorizontalGroup(
            jPanelAdresseRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAdresseRLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelReseau)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelReseauxText, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelAdresseRLayout.setVerticalGroup(
            jPanelAdresseRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAdresseRLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelAdresseRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelReseau)
                    .addComponent(jLabelReseauxText))
                .addContainerGap(170, Short.MAX_VALUE))
        );

        jTabbedPaneInfo.addTab("Reseau", jPanelAdresseR);

        jList1.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(jList1);

        jLabelNbHote.setText("Nombre d'hôte:");

        jLabelNbHoteText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelNbHoteText.setText("none");

        javax.swing.GroupLayout jPanelNbHoteLayout = new javax.swing.GroupLayout(jPanelNbHote);
        jPanelNbHote.setLayout(jPanelNbHoteLayout);
        jPanelNbHoteLayout.setHorizontalGroup(
            jPanelNbHoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNbHoteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelNbHoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanelNbHoteLayout.createSequentialGroup()
                        .addComponent(jLabelNbHote)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelNbHoteText, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelNbHoteLayout.setVerticalGroup(
            jPanelNbHoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNbHoteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelNbHoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNbHote)
                    .addComponent(jLabelNbHoteText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneInfo.addTab("Hôte", jPanelNbHote);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextFieldMasque, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jTextFieldIP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelPoint1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldIP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelPoint2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldIP3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelPoint3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldIP4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTabbedPaneInfo)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jButtonValider)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextFieldIP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldIP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldIP3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldIP4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelPoint1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addComponent(jLabelPoint2, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabelPoint3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldMasque, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonValider)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPaneInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldIP1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldIP1KeyPressed
        InitNumber(jTextFieldIP1);
    }//GEN-LAST:event_jTextFieldIP1KeyPressed

    /**
     * Modification du label jLabelClassLetter en fonction du nombre contenu
     * dedans Affiche la classe du premier octet ou rectifie la valeur du
     * jTextField si ce dernier dépasse 255
     *
     * @param evt
     */
    private void jTextFieldIP1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldIP1FocusLost
        VerifNumber(jTextFieldIP1);
        try {
            int firstOctet = Integer.parseInt(jTextFieldIP1.getText());
            if (firstOctet < 128) {
                jLabelClassLetter.setText("A");
                jTextFieldMasque.setText("255.0.0.0");
            } else if (firstOctet > 127 && firstOctet < 192) {
                jLabelClassLetter.setText("B");
                jTextFieldMasque.setText("255.255.0.0");
            } else if (firstOctet > 191 && firstOctet < 224) {
                jLabelClassLetter.setText("C");
                jTextFieldMasque.setText("255.255.255.0");
            } else if (firstOctet > 223 && firstOctet < 240) {
                jLabelClassLetter.setText("D");
                jTextFieldMasque.setText("255.255.255.255");
            } else if (firstOctet > 239 && firstOctet < 256) {
                jLabelClassLetter.setText("E");
            }

            if (firstOctet > 255) {
                jTextFieldIP1.setText("");
                jTextFieldIP1.setText("0");
            } else if (firstOctet < 1) {
                jTextFieldIP1.setText("");
                jTextFieldIP1.setText("0");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(jLabelClassText, e);

        }

    }//GEN-LAST:event_jTextFieldIP1FocusLost
    /**
     * Initialise la valeur du jTextField à "" (champ vide) au gain du focus
     * effectif pour tous les jTextField
     * @param evt 
     */
    private void jTextFieldIP2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldIP2KeyPressed
        InitNumber(jTextFieldIP2);
    }//GEN-LAST:event_jTextFieldIP2KeyPressed
    /**
     * A la perte du focus verifie si la valeur du champ est égal à ""(champ vide) dans ce cas affecte la valeur "0" (string et non int)
     * effectif pour tous les jTextField
     * @param evt 
     */
    private void jTextFieldIP2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldIP2FocusLost
        VerifNumber(jTextFieldIP2);
        VerifChamp(jTextFieldIP2);
    }//GEN-LAST:event_jTextFieldIP2FocusLost

    private void jTextFieldIP3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldIP3FocusLost
        VerifNumber(jTextFieldIP3);
        VerifChamp(jTextFieldIP3);
    }//GEN-LAST:event_jTextFieldIP3FocusLost

    private void jTextFieldIP3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldIP3KeyPressed
        InitNumber(jTextFieldIP3);
    }//GEN-LAST:event_jTextFieldIP3KeyPressed

    private void jTextFieldIP4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldIP4FocusLost
        VerifNumber(jTextFieldIP4);
        VerifChamp(jTextFieldIP4);
    }//GEN-LAST:event_jTextFieldIP4FocusLost

    private void jTextFieldIP4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldIP4KeyPressed
        InitNumber(jTextFieldIP4);
    }//GEN-LAST:event_jTextFieldIP4KeyPressed

    private void jTextFieldMasqueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMasqueKeyPressed
        InitNumber(jTextFieldMasque);
    }//GEN-LAST:event_jTextFieldMasqueKeyPressed

    private void jTextFieldMasqueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldMasqueFocusLost
        VerifNumber(jTextFieldMasque);
    }//GEN-LAST:event_jTextFieldMasqueFocusLost
    /**
     * Au clic vide entièrement la liste de la machine et appelle la fonction CalculIP()
     * @param evt 
     */
    private void jButtonValiderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonValiderMouseClicked
        DefaultListModel leModel= (DefaultListModel)jList1.getModel(); 
        leModel.clear();
        CalculIP();
    }//GEN-LAST:event_jButtonValiderMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonValider;
    private javax.swing.JLabel jLabelClassLetter;
    private javax.swing.JLabel jLabelClassText;
    private javax.swing.JLabel jLabelNbHote;
    private javax.swing.JLabel jLabelNbHoteText;
    private javax.swing.JLabel jLabelPoint1;
    private javax.swing.JLabel jLabelPoint2;
    private javax.swing.JLabel jLabelPoint3;
    private javax.swing.JLabel jLabelReseau;
    private javax.swing.JLabel jLabelReseauxText;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanelAdresseR;
    private javax.swing.JPanel jPanelClasse;
    private javax.swing.JPanel jPanelNbHote;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPaneInfo;
    private javax.swing.JTextField jTextFieldIP1;
    private javax.swing.JTextField jTextFieldIP2;
    private javax.swing.JTextField jTextFieldIP3;
    private javax.swing.JTextField jTextFieldIP4;
    private javax.swing.JTextField jTextFieldMasque;
    // End of variables declaration//GEN-END:variables
}
