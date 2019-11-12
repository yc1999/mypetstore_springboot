package org.csu.mypetstore_springboot.persistence;

import org.csu.mypetstore_springboot.domain.Sequence;

public interface SequenceMapper {
    Sequence getSequence(Sequence sequence);

    void updateSequence(Sequence sequence);
}
