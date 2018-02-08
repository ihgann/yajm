package com.ihgann.yajm.mapper;

import com.ihgann.yajm.mapper.objs.*;
import org.junit.Test;

import java.util.UUID;

import static com.ihgann.yajm.mapper.YajmUtils.map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestYajmUtils {

    @Test
    public void testBasicMapping() throws Throwable {
        IdOnlyObject idOnlyObject = new IdOnlyObject().withId(UUID.randomUUID().toString());

        // IdOnlyObject should map to IdOnlyObjectB
        IdOnlyObjectB idOnlyObjectB = map(idOnlyObject, IdOnlyObjectB.class);
        assertEquals(idOnlyObject.id, idOnlyObjectB.getId());

        // IdOnlyObjectB should map to IdOnlyObject
        IdOnlyObject remapped = map(idOnlyObjectB, IdOnlyObject.class);
        assertEquals(idOnlyObjectB.getId(), remapped.id);
    }

    @Test
    public void testMappingWithMissingFields() throws Throwable {
        IdAndNameObject idAndNameObject = new IdAndNameObject()
                .withId(UUID.randomUUID().toString())
                .withName(UUID.randomUUID().toString());

        // IdAndNameObject should be able to map `id` to IdOnlyObject
        IdOnlyObject idOnlyObject = map(idAndNameObject, IdOnlyObject.class);
        assertEquals(idAndNameObject.id, idOnlyObject.id);

        // IdOnlyObject can map back to IdAndNameObject, but cannot set the `name`
        IdAndNameObject remapped = map(idOnlyObject, IdAndNameObject.class);
        assertEquals(idOnlyObject.id, remapped.id);
        assertNull(remapped.name);
    }

    @Test
    public void testMappingWithDifferentNames() throws Throwable {
        IdAndNameObject idAndNameObject = new IdAndNameObject()
                .withId(UUID.randomUUID().toString())
                .withName(UUID.randomUUID().toString());

        // IdAndNameObject should be able to map completely to IdbAndNamebObject
        IdbAndNamebObject idbAndNamebObject = map(idAndNameObject, IdbAndNamebObject.class);
        assertEquals(idAndNameObject.id, idbAndNamebObject.idb);
        assertEquals(idAndNameObject.name, idbAndNamebObject.nameb);

        // IdbAndNamebObject should be able to map back to IdAndNameObject
        IdAndNameObject remapped = map(idbAndNamebObject, IdAndNameObject.class);
        assertEquals(idbAndNamebObject.idb, remapped.id);
        assertEquals(idbAndNamebObject.nameb, remapped.name);
    }

    @Test
    public void testMappingWithDissimilarNamesAndNoMappingAnnotation() throws Throwable {
        IdAndNameObject idAndNameObject = new IdAndNameObject()
                .withId(UUID.randomUUID().toString())
                .withName(UUID.randomUUID().toString());

        // IdAndNameObject should not be able to map to IdcAndNamecNoMappingsObject
        IdcAndNamecNoMappingsObject remapped = map(idAndNameObject, IdcAndNamecNoMappingsObject.class);
        assertNull(remapped.idc);
        assertNull(remapped.namec);

        IdcAndNamecNoMappingsObject newIdcAndNamecNoMappingsObject = new IdcAndNamecNoMappingsObject()
                .withIdc(UUID.randomUUID().toString())
                .withNamec(UUID.randomUUID().toString());

        // IdcAndNamecNoMappingsObject should not be able to map to IdAndNameObject
        IdAndNameObject remappedIdAndNameObject = map(newIdcAndNamecNoMappingsObject, IdAndNameObject.class);
        assertNull(remappedIdAndNameObject.id);
        assertNull(remappedIdAndNameObject.name);
    }

}
