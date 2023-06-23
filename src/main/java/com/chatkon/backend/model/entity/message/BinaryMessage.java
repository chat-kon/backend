package com.chatkon.backend.model.entity.message;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn
public class BinaryMessage extends Message {
    private String name;
    private String caption;
    private Byte[] data;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryMessage that = (BinaryMessage) o;
        return Objects.equals(name, that.name) && Objects.equals(caption, that.caption) && Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, caption);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
