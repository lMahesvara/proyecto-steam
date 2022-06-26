package guis;

import dominio.Usuario;
import dominio.Compra;
import dominio.DetalleCompra;
import excepciones.PersistenciaException;
import interfaces.IComprasDAO;
import interfaces.IUsuariosDAO;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Erick
 */
public class BusquedaComprasFrame extends javax.swing.JFrame {
    
    private IComprasDAO comprasDAO;
    private IUsuariosDAO usuariosDAO;
    private final NumberFormat FORMATODINERO = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
    
    public BusquedaComprasFrame(IComprasDAO comprasDAO, IUsuariosDAO usuariosDAO) {
        initComponents();
        this.comprasDAO = comprasDAO;
        this.usuariosDAO = usuariosDAO;
        generarComboboxUsuarios();
        limpiarCampos();
        generarTablaCompras();
    }
    
    private void llenarCamposCompra(Compra compra){
        txtUsuario.setText(compra.getUsuario().getNombre());
        txtFecha.setText(compra.getFechaCompra().getTime().toLocaleString());
        float subtotal = compra.getTotal()/1.16f;
        txtSubtotal.setText(FORMATODINERO.format(subtotal));
        txtTotal.setText(FORMATODINERO.format(compra.getTotal()));
    }
    
    private void generarTablaDetalles(){
        int indiceColumnaId = 0;
        int indiceFilaSeleccionada = this.tblCompras.getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel)this.tblDetallesCompra.getModel();
        long idCompra= (long)tblCompras.getValueAt(indiceFilaSeleccionada, indiceColumnaId);
        
        try {
            Compra compra = comprasDAO.consultar(idCompra);
            llenarCamposCompra(compra);
            List<DetalleCompra> detalles = compra.getVideojuegos();
            modelo.setRowCount(0);
            detalles.forEach(detalle -> {
                Object[] fila = {
                    detalle.getVideojuego().getId(),
                    detalle.getVideojuego().getNombre(),
                    detalle.getNumeroCopias(),
                    FORMATODINERO.format(detalle.getPrecio()),
                    FORMATODINERO.format(detalle.getImporte())
                };
                modelo.addRow(fila);
            });
        } catch (PersistenciaException ex) {
            Logger.getLogger(BusquedaComprasFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void generarComboboxUsuarios(){
        try {
            List<Usuario> zoologicos = usuariosDAO.consultarTodos();
            DefaultComboBoxModel modelo = (DefaultComboBoxModel) this.cbxUsuario.getModel();
            modelo.removeAllElements();
            modelo.addElement("Todos");
            zoologicos.forEach(user -> {
                modelo.addElement(user);
            });
        } catch (PersistenciaException ex) {
            Logger.getLogger(BusquedaComprasFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String validarBuscar(){
        if(dpFechaInicio.getText().isEmpty() || dpFechaInicio.getText() == null)
            return "Se requiere la fecha Desde";
        if(dpFechaFin.getText().isEmpty() || dpFechaFin.getText() == null)
            return "Se requiere la fecha Hasta";
        if(dpFechaInicio.getDate().isAfter(dpFechaFin.getDate()))
            return "La fecha Desde no puede ser mayor a la Hasta";
        return null;
    }
     
    private void generarTablaCompras(){
        try {
            String datosInvalidos = validarBuscar();
            if(datosInvalidos != null){
                JOptionPane.showMessageDialog(this,datosInvalidos,"Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            limpiarDetallesCompra();
            Calendar fechaInicio = Calendar.getInstance();
            fechaInicio.set(dpFechaInicio.getDate().getYear(), dpFechaInicio.getDate().getMonthValue()-1, dpFechaInicio.getDate().getDayOfMonth(),0,0,0);
            Calendar fechaFin = Calendar.getInstance();
            fechaFin.set(dpFechaFin.getDate().getYear(), dpFechaFin.getDate().getMonthValue()-1, dpFechaFin.getDate().getDayOfMonth(),23,59,59);
            Usuario user = cbxUsuario.getSelectedIndex() != 0 ? (Usuario) cbxUsuario.getSelectedItem() : null;
            
            List<Compra> compras = comprasDAO.consultarTodos(fechaInicio, fechaFin, user);
            DefaultTableModel modelo = (DefaultTableModel) this.tblCompras.getModel();
            modelo.setRowCount(0);
            compras.forEach(compra -> {
                Object[] fila = {
                    compra.getId(),
                    compra.getUsuario(),
                    compra.getFechaCompra().getTime().toLocaleString(),
                    FORMATODINERO.format(compra.getTotal())
                };
                modelo.addRow(fila);
            });
        } catch (PersistenciaException ex) {
            Logger.getLogger(BusquedaComprasFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarDetallesCompra(){
        txtUsuario.setText("");
        txtSubtotal.setText("");
        txtTotal.setText("");
        txtFecha.setText("");
        tblCompras.clearSelection();
        ((DefaultTableModel)tblDetallesCompra.getModel()).setRowCount(0);
    }
    
    private void limpiarCampos(){
        cbxUsuario.setSelectedIndex(0);
        dpFechaInicio.setDate(LocalDate.now());
        dpFechaFin.setDate(LocalDate.now());
        limpiarDetallesCompra();
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
        jPanel3 = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        txtUsuario = new javax.swing.JTextField();
        lblFecha = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        panTablaCompras = new javax.swing.JScrollPane();
        tblCompras = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        panTablaDetallesCompra = new javax.swing.JScrollPane();
        tblDetallesCompra = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        btnRegresar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblUsuarioCompra = new javax.swing.JLabel();
        cbxUsuario = new javax.swing.JComboBox<>();
        lblFechaInicio = new javax.swing.JLabel();
        lblFechaFin = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        dpFechaInicio = new com.github.lgooddatepicker.components.DatePicker();
        dpFechaFin = new com.github.lgooddatepicker.components.DatePicker();
        btnLimpiar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Administración de Búsqueda de Compras");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblUsuario.setText("Usuario");
        lblUsuario.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        lblSubtotal.setText("Subtotal");
        lblSubtotal.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        lblTotal.setText("Total");
        lblTotal.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        txtSubtotal.setEditable(false);
        txtSubtotal.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        txtUsuario.setEditable(false);
        txtUsuario.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        lblFecha.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lblFecha.setText("Fecha");

        txtFecha.setEditable(false);
        txtFecha.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsuario))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSubtotal))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFecha))
                .addGap(15, 15, 15))
        );

        tblCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Usuario", "Fecha", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCompras.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblComprasMouseClicked(evt);
            }
        });
        panTablaCompras.setViewportView(tblCompras);

        jLabel4.setText("Tabla de compras");
        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        tblDetallesCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombre", "Cantidad", "Precio", "Importe"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
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
        panTablaDetallesCompra.setViewportView(tblDetallesCompra);

        jLabel5.setText("Detalles de la compra");
        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        btnRegresar.setText("Regresar");
        btnRegresar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblUsuarioCompra.setText("Usuario");
        lblUsuarioCompra.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        cbxUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cbxUsuario.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        lblFechaInicio.setText("Desde que fecha: ");
        lblFechaInicio.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        lblFechaFin.setText("Hasta que fecha:");
        lblFechaFin.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        btnBuscar.setText("Buscar");
        btnBuscar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpiar))
                    .addComponent(dpFechaFin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dpFechaInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblFechaFin)
                    .addComponent(lblUsuarioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFechaInicio)
                    .addComponent(cbxUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblUsuarioCompra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbxUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(lblFechaInicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dpFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblFechaFin)
                .addGap(12, 12, 12)
                .addComponent(dpFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar)
                    .addComponent(btnLimpiar))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(btnRegresar)))
                .addGap(20, 38, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panTablaCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panTablaDetallesCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(257, 257, 257))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(243, 243, 243))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panTablaCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel5))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(panTablaDetallesCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRegresar)
                        .addGap(39, 39, 39))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        generarTablaCompras();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void tblComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblComprasMouseClicked
        generarTablaDetalles();
    }//GEN-LAST:event_tblComprasMouseClicked

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        new MenuFrame().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cbxUsuario;
    private com.github.lgooddatepicker.components.DatePicker dpFechaFin;
    private com.github.lgooddatepicker.components.DatePicker dpFechaInicio;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFechaFin;
    private javax.swing.JLabel lblFechaInicio;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblUsuarioCompra;
    private javax.swing.JScrollPane panTablaCompras;
    private javax.swing.JScrollPane panTablaDetallesCompra;
    private javax.swing.JTable tblCompras;
    private javax.swing.JTable tblDetallesCompra;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
