package guis;

import dominio.Videojuego;
import excepciones.PersistenciaException;
import interfaces.IVideojuegosDAO;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class VideojuegosFrame extends javax.swing.JFrame {

    private IVideojuegosDAO videojuegosDAO;
    
    public VideojuegosFrame(IVideojuegosDAO videojuegosDAO) {
        initComponents();
        this.videojuegosDAO = videojuegosDAO;
        generarTablaVideojuegos();
        txtNombre.requestFocus();
    }
    
    private void generarTablaVideojuegos(){
        try {
            List<Videojuego> videojuegos = videojuegosDAO.consultarTodos();
            DefaultTableModel modelo = (DefaultTableModel)this.tblVideojuegos.getModel();
            modelo.setRowCount(0);
            
            videojuegos.forEach(vj -> {
                Object[] fila = {
                    vj.getId(),
                    vj.getNombre(),
                    vj.getDesarrolladora(),
                    vj.getStock(),
                    vj.getPrecio()
                };
                modelo.addRow(fila);
            });
        } catch (PersistenciaException ex) {
            Logger.getLogger(VideojuegosFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void verDetalles(){
        int indiceColumnaId = 0;
        int indiceFilaSeleccionada = this.tblVideojuegos.getSelectedRow();
        if(indiceFilaSeleccionada == -1){
            JOptionPane.showMessageDialog(this,"Debes seleccionar un videojuego", "Información",  JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel)this.tblVideojuegos.getModel();
        long idVideojuego= (long)modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId);
        
        try {
            Videojuego videojuego = videojuegosDAO.consultar(idVideojuego);
            txtIdVideojuego.setText(videojuego.getId().toString());
            txtNombre.setText(videojuego.getNombre());
            txtDesarrolladora.setText(videojuego.getDesarrolladora());
            txtPrecio.setText(Float.toString(videojuego.getPrecio()));
            txtStock.setText(Integer.toString(videojuego.getStock()));
            btnGuardar.setText("Actualizar");
        } catch (PersistenciaException ex) {
            Logger.getLogger(VideojuegosFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarCampos(){
        txtIdVideojuego.setText("");
        txtNombre.setText("");
        txtDesarrolladora.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        btnGuardar.setText("Agregar");
        tblVideojuegos.clearSelection();
    }
    
    private void eliminar(){
         int indiceColumnaId = 0;
        int indiceFilaSeleccionada = this.tblVideojuegos.getSelectedRow();
        if(indiceFilaSeleccionada == -1){
            JOptionPane.showMessageDialog(this,"Debes seleccionar un videojuego", "Información",  JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel)this.tblVideojuegos.getModel();
        long idVideojuego= (long)modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId);
        
        int opcion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el Animal?","Eliminar",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if(opcion == JOptionPane.YES_OPTION){
             try {
                 videojuegosDAO.eliminar(idVideojuego);
                 JOptionPane.showMessageDialog(this, "Videojuego eliminado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                 generarTablaVideojuegos();
                 limpiarCampos();
             } catch (PersistenciaException ex) {
                 Logger.getLogger(VideojuegosFrame.class.getName()).log(Level.SEVERE, null, ex);
                 JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
             }
        }
    }
    
    private void actualizar(){
        String datosInvalidos = validarAgregar();
        if(datosInvalidos != null){
            JOptionPane.showMessageDialog(this,datosInvalidos,"Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Long idVideojuego = Long.parseLong(txtIdVideojuego.getText());
        String nombre = txtNombre.getText();
        String desarrolladora = txtDesarrolladora.getText();
        int stock = Integer.parseInt(txtStock.getText());
        float precio = Float.parseFloat(txtPrecio.getText());
        Videojuego videojuego = new Videojuego(idVideojuego, nombre, desarrolladora, stock, precio);
        
        try {
            videojuegosDAO.actualizar(videojuego);
            JOptionPane.showMessageDialog(this, "Videojuego actualizado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
            this.generarTablaVideojuegos();
            this.limpiarCampos();
        } catch (PersistenciaException ex) {
            Logger.getLogger(VideojuegosFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void agregar(){
        String datosInvalidos = validarAgregar();
        if(datosInvalidos != null){
            JOptionPane.showMessageDialog(this,datosInvalidos,"Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String nombre = txtNombre.getText();
        String desarrolladora = txtDesarrolladora.getText();
        int stock = Integer.parseInt(txtStock.getText());
        float precio = Float.parseFloat(txtPrecio.getText());
        Videojuego videojuego = new Videojuego(nombre, desarrolladora, stock, precio);
        
        try {
            videojuegosDAO.agregar(videojuego);
            JOptionPane.showMessageDialog(this, "Videojuego agregado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
            this.generarTablaVideojuegos();
            this.limpiarCampos();
        } catch (PersistenciaException ex) {
            Logger.getLogger(VideojuegosFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void guardar(){
        if(txtIdVideojuego.getText().isEmpty()) agregar();
        else actualizar();
    }
    
    private String validarAgregar(){
        //Validaciones de campos vacíos
        if(txtNombre.getText() == null || txtNombre.getText().isEmpty())
            return "Se requiere el nombre";
        if(txtDesarrolladora.getText() == null || txtDesarrolladora.getText().isEmpty())
            return "Se requiere la desarrolladora";
        if(txtStock.getText() == null || txtStock.getText().isEmpty())
            return "Se requiere el stock";
        if(txtPrecio.getText() == null || txtPrecio.getText().isEmpty())
            return "Se requiere el precio";
        
        //Validación de formato
        if(txtNombre.getText().length() > 50)
            return "El nombre no puede superar los 50 caracteres";
        if(txtDesarrolladora.getText().length() > 100)
            return "La desarrolladora no puede superar los 100 caracteres";
        
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtPrecio = new javax.swing.JTextField();
        lblPrecio = new javax.swing.JLabel();
        lblStock = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        txtDesarrolladora = new javax.swing.JTextField();
        lblDesarrolladora = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtIdVideojuego = new javax.swing.JTextField();
        lblIdVideojuego = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panTablaVideojuegos = new javax.swing.JScrollPane();
        tblVideojuegos = new javax.swing.JTable();
        btnRegresar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Administración de Videojuegos");
        setResizable(false);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnGuardar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnGuardar.setText("Agregar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        txtPrecio.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtPrecio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrecioFocusLost(evt);
            }
        });
        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioKeyTyped(evt);
            }
        });

        lblPrecio.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblPrecio.setText("Precio");

        lblStock.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblStock.setText("Stock");

        txtStock.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockKeyTyped(evt);
            }
        });

        txtDesarrolladora.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtDesarrolladora.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDesarrolladoraKeyTyped(evt);
            }
        });

        lblDesarrolladora.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblDesarrolladora.setText("Desarrolladora");

        lblNombre.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblNombre.setText("Nombre");

        txtNombre.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombreFocusLost(evt);
            }
        });
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        txtIdVideojuego.setEditable(false);
        txtIdVideojuego.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        lblIdVideojuego.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblIdVideojuego.setText("Id Videojuego");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(lblIdVideojuego, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDesarrolladora, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblStock, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPrecio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombre)
                            .addComponent(txtDesarrolladora)
                            .addComponent(txtIdVideojuego, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStock)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(btnGuardar)
                        .addGap(58, 58, 58)
                        .addComponent(btnCancelar)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdVideojuego)
                    .addComponent(txtIdVideojuego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDesarrolladora)
                    .addComponent(txtDesarrolladora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStock)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrecio)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnGuardar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblVideojuegos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombre", "Desarrolladora", "Stock", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblVideojuegos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblVideojuegos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVideojuegosMouseClicked(evt);
            }
        });
        panTablaVideojuegos.setViewportView(tblVideojuegos);

        btnRegresar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnRegresar.setText("Regresar");

        btnEliminar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panTablaVideojuegos, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnEliminar)
                        .addGap(206, 206, 206)
                        .addComponent(btnRegresar))
                    .addComponent(panTablaVideojuegos, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 797, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tblVideojuegosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVideojuegosMouseClicked
        verDetalles();
    }//GEN-LAST:event_tblVideojuegosMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockKeyTyped
        char c = evt.getKeyChar();
        if(!Character.isDigit(c) || txtStock.getText().length() >=10){
            evt.consume();
        }
    }//GEN-LAST:event_txtStockKeyTyped

    private void txtPrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioKeyTyped
        char c = evt.getKeyChar();
        String precio = txtPrecio.getText();
        if(c == KeyEvent.VK_PERIOD && precio.contains("."))
            evt.consume();
        if(precio.contains(".") && !precio.endsWith(".")){
            String[] partes = precio.split("\\.");
            if(partes[1].length() >= 2) evt.consume();
        }
        if(!(Character.isDigit(c) || c == KeyEvent.VK_PERIOD) || precio.length() >=10){
            evt.consume();
        }
    }//GEN-LAST:event_txtPrecioKeyTyped

    private void txtPrecioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrecioFocusLost
        if(txtPrecio.getText().isEmpty())return;
        
        String regExp = "[0-9]+([.][0-9]{1,2})?";
        if(!txtPrecio.getText().matches(regExp)){
            JOptionPane.showMessageDialog(this,"El precio NO cumple con el formato","Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txtPrecioFocusLost

    private void txtDesarrolladoraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDesarrolladoraKeyTyped
        char c = evt.getKeyChar();
        if(txtStock.getText().length() >=100){
            evt.consume();
        }
    }//GEN-LAST:event_txtDesarrolladoraKeyTyped

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        char c = evt.getKeyChar();
        if(txtStock.getText().length() >=50){
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtNombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreFocusLost
        String nombre = txtNombre.getText();
        if(nombre == null || nombre.isEmpty())
            return;
        try {
            Videojuego videojuego = videojuegosDAO.consultar(nombre);
            if(videojuego != null && txtIdVideojuego.getText().isEmpty()){
                JOptionPane.showMessageDialog(this,"El nombre del videojuego ya existe", "Información",  JOptionPane.WARNING_MESSAGE);
                txtNombre.requestFocus();
            }
        } catch (PersistenciaException ex) {
            Logger.getLogger(VideojuegosFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtNombreFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblDesarrolladora;
    private javax.swing.JLabel lblIdVideojuego;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblStock;
    private javax.swing.JScrollPane panTablaVideojuegos;
    private javax.swing.JTable tblVideojuegos;
    private javax.swing.JTextField txtDesarrolladora;
    private javax.swing.JTextField txtIdVideojuego;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
