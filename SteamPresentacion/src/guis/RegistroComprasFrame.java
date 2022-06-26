package guis;

import dominio.Compra;
import static dominio.Compra_.usuario;
import dominio.DetalleCompra;
import dominio.Usuario;
import dominio.Videojuego;
import excepciones.PersistenciaException;
import interfaces.IComprasDAO;
import interfaces.IUsuariosDAO;
import interfaces.IVideojuegosDAO;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
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
public class RegistroComprasFrame extends javax.swing.JFrame {

    private IVideojuegosDAO videojuegosDAO;
    private IUsuariosDAO usuariosDAO;
    private IComprasDAO comprasDAO;

    public RegistroComprasFrame(IVideojuegosDAO videojuegosDAO, IUsuariosDAO usuariosDAO, IComprasDAO comprasDAO) {
        initComponents();
        this.videojuegosDAO = videojuegosDAO;
        this.usuariosDAO = usuariosDAO;
        this.comprasDAO = comprasDAO;
        generarTablaVideojuegos("");
        generarComboboxUsuarios();
        generarCostos();
    }

    private float generarCostos() {
        DefaultTableModel modelo = (DefaultTableModel) this.tblDetallesCompra.getModel();
        Float subtotal = 0f;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            subtotal += (float) modelo.getValueAt(i, 4);
        }
        float total = subtotal * 1.16f;
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
        txtSubtotal.setText(formatoImporte.format(subtotal));
        txtTotal.setText(formatoImporte.format(total));
        return total;
    }

    private void agregarVideojuegoSeleccionado(Videojuego videojuego, int cantidadNueva) {
        DefaultTableModel modelo = (DefaultTableModel) this.tblDetallesCompra.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0) != videojuego.getId()) {
                continue;
            }

            int cantidadActual = (int) modelo.getValueAt(i, 2);
            if (cantidadNueva + cantidadActual > videojuego.getStock()) {
                JOptionPane.showMessageDialog(this, "La cantidad supera el stock actual", "Información", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int cantidadTotal = cantidadActual + cantidadNueva;
            modelo.setValueAt(cantidadTotal, i, 2);
            modelo.setValueAt((cantidadTotal * videojuego.getPrecio()), i, 4);
            return;
        }
        Object[] fila = {
            videojuego.getId(),
            videojuego,
            cantidadNueva,
            videojuego.getPrecio(),
            (cantidadNueva * videojuego.getPrecio())
        };
        modelo.addRow(fila);

    }

    private void generarComboboxUsuarios() {
        try {
            List<Usuario> zoologicos = usuariosDAO.consultarTodos();
            DefaultComboBoxModel modelo = (DefaultComboBoxModel) this.cbxUsuario.getModel();
            modelo.removeAllElements();
            modelo.addElement("Seleccione un usuario");
            zoologicos.forEach(user -> {
                modelo.addElement(user);
            });
        } catch (PersistenciaException ex) {
            Logger.getLogger(RegistroComprasFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarTablaVideojuegos(String nombre) {
        try {
            List<Videojuego> videojuegos = videojuegosDAO.consultarTodos(nombre);
            DefaultTableModel modelo = (DefaultTableModel) this.tblVideojuegos.getModel();
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
            Logger.getLogger(RegistroComprasFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void limpiarCampos(){
        txtSubtotal.setText("");
        txtTotal.setText("");
        cbxUsuario.setSelectedIndex(0);
        ((DefaultTableModel)tblDetallesCompra.getModel()).setRowCount(0);
        tblVideojuegos.clearSelection();
    }
    
    private String validarAgregar(){
        if(cbxUsuario.getSelectedIndex() == 0)
            return "Seleccione algún usuario";
        if(tblDetallesCompra.getRowCount() == 0)
            return "Seleccione al menos un videojuego";
        return null;
    }
    
    private void agregar() {
        String datosInvalidos = validarAgregar();
        if(datosInvalidos != null){
            JOptionPane.showMessageDialog(this,datosInvalidos,"Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Usuario usuario = (Usuario) cbxUsuario.getSelectedItem();
        float total = generarCostos();
        Calendar fecha = Calendar.getInstance();
        Compra compra = new Compra(fecha, total, usuario);

        for (int i = 0; i < tblDetallesCompra.getRowCount(); i++) {
            DetalleCompra dc = new DetalleCompra();
            dc.setNumeroCopias((int) tblDetallesCompra.getValueAt(i, 2));
            dc.setPrecio((float)tblDetallesCompra.getValueAt(i, 3));
            dc.setImporte((float)tblDetallesCompra.getValueAt(i, 4));
            dc.setCompra(compra);
            Videojuego videojuego = (Videojuego)tblDetallesCompra.getValueAt(i, 1);
            videojuego.setStock(videojuego.getStock()-dc.getNumeroCopias());
            try {
                videojuegosDAO.actualizar(videojuego);
            } catch (PersistenciaException ex) {
                Logger.getLogger(RegistroComprasFrame.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            dc.setVideojuego(videojuego);
            compra.addVideojuego(dc);
        }
        try {
            comprasDAO.agregar(compra);
            JOptionPane.showMessageDialog(this, "Compra realizada con éxito", "Información", JOptionPane.INFORMATION_MESSAGE);
            this.limpiarCampos();
            this.generarTablaVideojuegos("");
        } catch (PersistenciaException ex) {
            Logger.getLogger(RegistroComprasFrame.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        cbxUsuario = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblUsuario1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        panTablaVideojuegos = new javax.swing.JScrollPane();
        tblVideojuegos = new javax.swing.JTable();
        txtBuscador = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnRecargar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        panTablaDetallesCompra = new javax.swing.JScrollPane();
        tblDetallesCompra = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Administración de Registro de Compras");
        setResizable(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblUsuario.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lblUsuario.setText("Usuario");

        lblSubtotal.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lblSubtotal.setText("Subtotal");

        lblTotal.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lblTotal.setText("Total");

        txtSubtotal.setEditable(false);
        txtSubtotal.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        cbxUsuario.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        cbxUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un usuario" }));
        cbxUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxUsuarioActionPerformed(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnGuardar.setText("Guardar");
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

        lblUsuario1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        lblUsuario1.setText("Registro de Compra");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addGap(65, 65, 65)
                        .addComponent(btnCancelar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSubtotal)
                            .addComponent(txtTotal)
                            .addComponent(cbxUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblUsuario1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lblUsuario1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsuario)
                    .addComponent(cbxUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSubtotal)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotal)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Videojuegos Disponibles", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14))); // NOI18N

        tblVideojuegos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombre", "Desarrolladora", "Stock", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class
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
        tblVideojuegos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblVideojuegosKeyPressed(evt);
            }
        });
        panTablaVideojuegos.setViewportView(tblVideojuegos);

        txtBuscador.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtBuscador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscadorKeyReleased(evt);
            }
        });

        btnBuscar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnRecargar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnRecargar.setText("Limpiar lista");
        btnRecargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecargarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRecargar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panTablaVideojuegos, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(btnRecargar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panTablaVideojuegos, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Detalles de videojuegos seleccionados", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14))); // NOI18N

        tblDetallesCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombre", "Cantidad", "Precio", "Importe"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class
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
        tblDetallesCompra.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblDetallesCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetallesCompraMouseClicked(evt);
            }
        });
        panTablaDetallesCompra.setViewportView(tblDetallesCompra);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panTablaDetallesCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panTablaDetallesCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(191, 191, 191)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cbxUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxUsuarioActionPerformed

    private void detallesVideojuego(){
        int cantidad = 1;
        StringBuffer cantidadElegida = new StringBuffer();
        int indiceColumnaId = 0;
        int indiceFilaSeleccionada = this.tblVideojuegos.getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) this.tblVideojuegos.getModel();
        long idVideojuego = (long) modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId);

        try {
            Videojuego videojuego = videojuegosDAO.consultar(idVideojuego);

            new DlgCantidad(this, "Elegir cantidad", true, cantidadElegida);
            if (cantidadElegida.toString().isEmpty()) {
                return;
            }
            cantidad = Integer.parseInt(cantidadElegida.toString());
            if (cantidad > videojuego.getStock()) {
                JOptionPane.showMessageDialog(this, "La cantidad supera el stock actual", "Información", JOptionPane.WARNING_MESSAGE);
                return;
            }
            agregarVideojuegoSeleccionado(videojuego, cantidad);
            generarCostos();
        } catch (PersistenciaException ex) {
            Logger.getLogger(RegistroComprasFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void tblVideojuegosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVideojuegosMouseClicked
        detallesVideojuego();
    }//GEN-LAST:event_tblVideojuegosMouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        generarTablaVideojuegos(txtBuscador.getText().trim());
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnRecargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecargarActionPerformed
        txtBuscador.setText("");
        generarTablaVideojuegos("");
        txtBuscador.requestFocus();
    }//GEN-LAST:event_btnRecargarActionPerformed

    private void txtBuscadorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscadorKeyReleased
        generarTablaVideojuegos(txtBuscador.getText().trim());
    }//GEN-LAST:event_txtBuscadorKeyReleased

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        agregar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tblDetallesCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetallesCompraMouseClicked
        int opcion = JOptionPane.showConfirmDialog(this, "¿Está seguro de remover el videojuego?","Eliminar",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if(opcion == JOptionPane.YES_OPTION){
            int indiceFilaSeleccionada = this.tblDetallesCompra.getSelectedRow();
            DefaultTableModel modelo = (DefaultTableModel)this.tblDetallesCompra.getModel();
            modelo.removeRow(indiceFilaSeleccionada);
            generarCostos();
        }
    }//GEN-LAST:event_tblDetallesCompraMouseClicked

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        new MenuFrame().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tblVideojuegosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblVideojuegosKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            detallesVideojuego();
        }
    }//GEN-LAST:event_tblVideojuegosKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnRecargar;
    private javax.swing.JComboBox<String> cbxUsuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblUsuario1;
    private javax.swing.JScrollPane panTablaDetallesCompra;
    private javax.swing.JScrollPane panTablaVideojuegos;
    private javax.swing.JTable tblDetallesCompra;
    private javax.swing.JTable tblVideojuegos;
    private javax.swing.JTextField txtBuscador;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
