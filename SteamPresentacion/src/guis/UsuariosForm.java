package guis;

import dominio.Usuario;
import excepciones.PersistenciaException;
import implementaciones.UsuariosDAO;
import interfaces.IUsuariosDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Erick
 */
public class UsuariosForm extends javax.swing.JFrame {

    
    private final IUsuariosDAO usuariosDAO;
    
    
    /**
     * Creates new form UsuariosForm
     */
    public UsuariosForm(IUsuariosDAO usuariosDAO){
        initComponents();
        this.usuariosDAO = usuariosDAO;
        generarTablaUsuarios();
    }
    
    private void generarTablaUsuarios(){
        try{
            List<Usuario> usuarios = usuariosDAO.consultarTodos();
            DefaultTableModel modelo = (DefaultTableModel)this.tblUsuarios.getModel();
            modelo.setRowCount(0);
            
            usuarios.forEach(usuario -> {
            Object[] fila = {usuario.getId(), usuario.getNombre(), usuario.getTelefono()};
            modelo.addRow(fila);
            });
            
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void verDetalles(){
        int indiceColumnaId = 0;
        int indiceFilaSeleccionada = this.tblUsuarios.getSelectedRow();
        if(indiceFilaSeleccionada == -1){
            JOptionPane.showMessageDialog(this, "Debes seleccionar un usuario", "Información", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel)this.tblUsuarios.getModel();
        long idUsuario = (long)modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId);
        
        try{
            Usuario usuario = usuariosDAO.consultar(idUsuario);
            this.txtUsuario.setText(usuario.getId().toString());
            this.txtNombre.setText(usuario.getNombre());
            this.txtTelefono.setText(usuario.getTelefono());
            this.btnGuardar.setText("Actualizar");
            
            
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarCampos(){
        this.txtUsuario.setText("");
        this.txtNombre.setText("");
        this.txtTelefono.setText("");
        this.btnGuardar.setText("Agregar");
        tblUsuarios.clearSelection();
    }
    
    private void eliminar(){
        int indiceColumnaId = 0;
        int indiceFilaSeleccionada = this.tblUsuarios.getSelectedRow();
        if(indiceFilaSeleccionada == -1){
            JOptionPane.showMessageDialog(this,"Debes seleccionar un Usuario", "Información",  JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel)this.tblUsuarios.getModel();
        long idUsuario= (long)modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId); 
        
        int opcion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el Usuario?","Eliminar",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if(opcion == JOptionPane.YES_OPTION){
            try {
                usuariosDAO.eliminar(idUsuario);
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                generarTablaUsuarios();
            } catch (PersistenciaException ex) {
                Logger.getLogger(UsuariosForm.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void agregar(){
        String nombre = this.txtNombre.getText();
        String telefono = this.txtTelefono.getText();
        Usuario usuario= new Usuario(nombre, telefono);
        String datosInvalidos = validarAgregar(usuario);
        if(datosInvalidos != null){
            JOptionPane.showMessageDialog(this,datosInvalidos,"Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            usuariosDAO.agregar(usuario);
            JOptionPane.showMessageDialog(this, "Usuario agregada correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
            this.generarTablaUsuarios();
            this.limpiarCampos();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String validarAgregar(Usuario usuario){
         //Validaciones de formato de los campos
         if(usuario.getNombre().length() > 100)
            return "Ha excedido el límite de caracteres en el nombre";
        if(usuario.getTelefono().length() > 10)
            return "Ha excedido el límite de caracteres en el telefono";
        
        
        //Validaciones de campos vacíos
        if(usuario.getNombre()== null || usuario.getNombre().isEmpty())
            return "Se requiere el nombre";
        if(usuario.getTelefono()== null || usuario.getTelefono().isEmpty())
            return "Se requiere el telefono";
        return null;
    }
    
    private void actualizar(){
        Long idUsuario = Long.parseLong(this.txtUsuario.getText());
        String nombre = this.txtNombre.getText();
        String  telefono = this.txtTelefono.getText();
        Usuario usuario = new Usuario(idUsuario,nombre, telefono);
        String datosInvalidos = validarAgregar(usuario);
        if(datosInvalidos != null){
            JOptionPane.showMessageDialog(this,datosInvalidos,"Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            usuariosDAO.actualizar(usuario);
            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
            this.generarTablaUsuarios();
            this.limpiarCampos();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        lblUsuario = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        panTablaUsuarios = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Administración de Usuarios");
        setResizable(false);

        lblUsuario.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblUsuario.setText("Id Usuario");

        lblNombre.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblNombre.setText("Nombre");

        lblTelefono.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblTelefono.setText("Teléfono");

        txtUsuario.setEditable(false);
        txtUsuario.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        txtNombre.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        txtTelefono.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

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

        panTablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panTablaUsuariosMouseClicked(evt);
            }
        });

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombre", "Teléfono"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuariosMouseClicked(evt);
            }
        });
        panTablaUsuarios.setViewportView(tblUsuarios);

        btnEliminar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnRegresar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addComponent(txtTelefono)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(btnGuardar)
                        .addGap(50, 50, 50)
                        .addComponent(btnCancelar)))
                .addGap(27, 27, 27)
                .addComponent(panTablaUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminar)
                    .addComponent(btnRegresar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsuario)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTelefono)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addContainerGap(17, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRegresar))
                    .addComponent(panTablaUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if(btnGuardar.getText() == "Agregar"){
            agregar();
        } else{
            actualizar();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        new MenuFrame().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void panTablaUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panTablaUsuariosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panTablaUsuariosMouseClicked

    private void tblUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuariosMouseClicked
        verDetalles();
    }//GEN-LAST:event_tblUsuariosMouseClicked

    /**
     * 
     * @param args 
     */
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JScrollPane panTablaUsuarios;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

    

}

