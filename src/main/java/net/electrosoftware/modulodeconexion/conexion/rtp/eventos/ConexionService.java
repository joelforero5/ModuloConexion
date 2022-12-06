package net.electrosoftware.modulodeconexion.conexion.rtp.eventos;

public class ConexionService {

    String title;
    String fase;
    int porcentageProgreso;
    boolean canCancel;
    boolean canClosed;
    boolean cancelled = false;
    String error = "";

    public ConexionService(String title, String fase, boolean canCancel, boolean canClosed) {
        this.title = title;
        this.fase = fase;
        this.porcentageProgreso = 0;
        this.canCancel = canCancel;
        this.canClosed = canClosed;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isCanCancel() {
        return canCancel;
    }

    public void setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public int getPorcentageProgreso() {
        return porcentageProgreso;
    }

    public void setPorcentageProgreso(int porcentageProgreso) {
        this.porcentageProgreso = porcentageProgreso;
    }

    public boolean isCanClosed() {
        return canClosed;
    }

    public void setCanClosed(boolean canClosed) {
        this.canClosed = canClosed;
    }
}