package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

// Clase para clave compuesta de Presence/fichar
public class PresenceId implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private LocalDateTime enterTime;

    // default constructor

    public PresenceId(Integer id, LocalDateTime enterTime) {
        this.id = id;
        this.enterTime = enterTime;
    }
    public PresenceId() {
        this.id = null;
        this.enterTime = null;
    }

	@Override
	public int hashCode() {
		return Objects.hash(enterTime, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PresenceId other = (PresenceId) obj;
		return Objects.equals(enterTime, other.enterTime) && Objects.equals(id, other.id);
	}

    
    // equals() and hashCode()
}
