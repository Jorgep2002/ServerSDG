package org.example.shared.entities;

import java.io.Serializable;
import java.sql.Timestamp;

public class DirectorioEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idDirectorio; // Cambiado a Integer para permitir valores nulos
    private String dirNombre;
    private String fkIdPropietario;
    private String dirTipo; // "archivo" o "carpeta"
    private String dirRuta;
    private String dirExtension;
    private Timestamp dirFechaCreacion;
    private Timestamp dirFechaModificacion;
    private Integer fkIdGrupo;
    private Integer fkIdPadre;

    // Constructor vacío
    public DirectorioEntity() {
    }

    // Constructor sin ID
    public DirectorioEntity(String dirNombre, String fkIdPropietario, String dirTipo,
                            String dirRuta, String dirExtension, Timestamp dirFechaCreacion,
                            Timestamp dirFechaModificacion, Integer fkIdGrupo, Integer fkIdPadre) {
        this.dirNombre = dirNombre;
        this.fkIdPropietario = fkIdPropietario;
        this.dirTipo = dirTipo;
        this.dirRuta = dirRuta;
        this.dirExtension = dirExtension;
        this.dirFechaCreacion = dirFechaCreacion;
        this.dirFechaModificacion = dirFechaModificacion;
        this.fkIdGrupo = fkIdGrupo;
        this.fkIdPadre = fkIdPadre;
    }

    // Constructor con todos los atributos (incluido el ID)
    public DirectorioEntity(Integer idDirectorio, String dirNombre, String fkIdPropietario,
                            String dirTipo, String dirRuta, String dirExtension,
                            Timestamp dirFechaCreacion, Timestamp dirFechaModificacion,
                            Integer fkIdGrupo, Integer fkIdPadre) {
        this.idDirectorio = idDirectorio;
        this.dirNombre = dirNombre;
        this.fkIdPropietario = fkIdPropietario;
        this.dirTipo = dirTipo;
        this.dirRuta = dirRuta;
        this.dirExtension = dirExtension;
        this.dirFechaCreacion = dirFechaCreacion;
        this.dirFechaModificacion = dirFechaModificacion;
        this.fkIdGrupo = fkIdGrupo;
        this.fkIdPadre = fkIdPadre;
    }

    // Getters y Setters
    public Integer getIdDirectorio() {
        return idDirectorio;
    }

    public void setIdDirectorio(Integer idDirectorio) {
        this.idDirectorio = idDirectorio;
    }

    public String getDirNombre() {
        return dirNombre;
    }

    public void setDirNombre(String dirNombre) {
        this.dirNombre = dirNombre;
    }

    public String getFkIdPropietario() {
        return fkIdPropietario;
    }

    public void setFkIdPropietario(String fkIdPropietario) {
        this.fkIdPropietario = fkIdPropietario;
    }

    public String getDirTipo() {
        return dirTipo;
    }

    public void setDirTipo(String dirTipo) {
        this.dirTipo = dirTipo;
    }

    public String getDirRuta() {
        return dirRuta;
    }

    public void setDirRuta(String dirRuta) {
        this.dirRuta = dirRuta;
    }

    public String getDirExtension() {
        return dirExtension;
    }

    public void setDirExtension(String dirExtension) {
        this.dirExtension = dirExtension;
    }

    public Timestamp getDirFechaCreacion() {
        return dirFechaCreacion;
    }

    public void setDirFechaCreacion(Timestamp dirFechaCreacion) {
        this.dirFechaCreacion = dirFechaCreacion;
    }

    public Timestamp getDirFechaModificacion() {
        return dirFechaModificacion;
    }

    public void setDirFechaModificacion(Timestamp dirFechaModificacion) {
        this.dirFechaModificacion = dirFechaModificacion;
    }

    public Integer getFkIdGrupo() {
        return fkIdGrupo;
    }

    public void setFkIdGrupo(Integer fkIdGrupo) {
        this.fkIdGrupo = fkIdGrupo;
    }

    public Integer getFkIdPadre() {
        return fkIdPadre;
    }

    public void setFkIdPadre(Integer fkIdPadre) {
        this.fkIdPadre = fkIdPadre;
    }

    @Override
    public String toString() {
        return "DirectorioEntity{" +
                "idDirectorio=" + idDirectorio +
                ", dirNombre='" + dirNombre + '\'' +
                ", fkIdPropietario='" + fkIdPropietario + '\'' +
                ", dirTipo='" + dirTipo + '\'' +
                ", dirRuta='" + dirRuta + '\'' +
                ", dirExtension='" + dirExtension + '\'' +
                ", dirFechaCreacion=" + dirFechaCreacion +
                ", dirFechaModificacion=" + dirFechaModificacion +
                ", fkIdGrupo=" + fkIdGrupo +
                ", fkIdPadre=" + fkIdPadre +
                '}';
    }
}
