package infr.diff;

import domain.marker.Entity;
import domain.marker.Identifier;
import io.vavr.API;

import java.lang.reflect.Field;
import java.util.*;

import static io.vavr.API.$;
import static io.vavr.API.Case;

/**
 * @author xxs
 * @Date 2024/6/29 16:16
 * Change-Tracking
 */
public class DiffUtils {

    public static <T extends Entity<?>> EntityDiff diff(T oldValue, T newValue) {
        if (Objects.isNull(oldValue)) {
            throw new RuntimeException("old agg value is null, as no agg value attached before updated new value");
        }
        return diffEntity(null, oldValue, newValue);
    }


    /**
     * diffEntity
     * @param field
     * @param oldValue
     * @param newValue
     * @return
     */
    public static EntityDiff diffEntity(Field field, Entity<?> oldValue, Entity<?> newValue) {
        DiffBuilder diffBuilder = new DiffBuilder(field, oldValue, newValue);
        Class<? extends Entity> entityClass = newValue.getClass();
        for (Field subField : ReflectionUtils.getFields(entityClass)) {
            Class<?> subFieldType = subField.getType();
            Object oldVal = oldValue == null ? null : ReflectionUtils.readField(subField, oldValue);
            Object newVal = ReflectionUtils.readField(subField, newValue);
            DiffBuilder finalDiffBuilder = diffBuilder;
            diffBuilder = API.Match(fetchModifyEntityType(subFieldType, oldVal, newVal))
                    .of(Case(API.$(ModifyEntityType.MODIFY_LIST), () -> fetchModifyEntityListDiffBuilder(subField, oldVal, newVal, finalDiffBuilder)),
                           Case(API.$(ModifyEntityType.MODIFY_ENTITY), () -> fetchModifyEntityDiffBuilder(subField, oldVal, newVal, finalDiffBuilder)),
                            Case(API.$(ModifyEntityType.MODIFY_PROPERTY), () -> fetchModifyPropertyDiffBuilder(subField, oldVal, newVal, finalDiffBuilder)));

        }
        return diffBuilder.build();
    }


    /**
     * modifyList
     * @return
     */
    private static DiffBuilder fetchModifyEntityListDiffBuilder(Field subField, Object oldVal, Object newVal, DiffBuilder diffBuilder) {
        ListDiff listDiff = diffEntityList(subField, (List<Entity<?>>) oldVal, (List<Entity<?>>) newVal);
        diffBuilder.modifyList(listDiff);
        return diffBuilder;
    }


    /**
     * modify Entity
     * @return
     */
    private static DiffBuilder fetchModifyEntityDiffBuilder(Field subField, Object oldVal, Object newVal, DiffBuilder diffBuilder) {
        diffBuilder.modifyEntity(subField, (Entity<?>) oldVal, (Entity<?>) newVal);
        return diffBuilder;
    }


    /**
     * property
     * @param subField
     * @param oldVal
     * @param newVal
     * @param diffBuilder
     * @return
     */
    private static DiffBuilder fetchModifyPropertyDiffBuilder(Field subField, Object oldVal, Object newVal, DiffBuilder diffBuilder) {
        diffBuilder.modifyProperty(subField, oldVal, newVal);
        return diffBuilder;

    }


    private static ModifyEntityType fetchModifyEntityType(Class<?> subFieldType, Object oldVal, Object newVal) {
        // list
        if (List.class.isAssignableFrom(subFieldType) &&
                ((ReflectionUtils.isListItemAssignableTo(Entity.class, (List<?>) oldVal)))) {
            return ModifyEntityType.MODIFY_LIST;
        }
        // entity
        if ((Objects.nonNull(oldVal) && Entity.class.isAssignableFrom(oldVal.getClass())) || (
                Objects.nonNull(newVal) && Entity.class.isAssignableFrom(newVal.getClass()))) {
            return ModifyEntityType.MODIFY_ENTITY;
        }
        return ModifyEntityType.MODIFY_PROPERTY;

    }

    /**
     * entity list
     * @param field
     * @param oldValues
     * @param newValues
     * @return
     */
    public static ListDiff diffEntityList(Field field, List<Entity<?>> oldValues, List<Entity<?>> newValues) {
        DiffBuilder diffBuilder = new DiffBuilder(field, oldValues, newValues);
        Map<Identifier, Entity<?>> oldMap = buildMap(oldValues);
        Map<Identifier, Entity<?>> newMap = buildMap(newValues);

        Set<Identifier> allKeys = new HashSet<>(oldMap.keySet());
        allKeys.addAll(newMap.keySet());

        for (Identifier key: allKeys) {
            Entity<?> oldEntity = oldMap.get(key);
            Entity<?> newEntity = newMap.get(key);

            if (Objects.nonNull(oldEntity) && Objects.isNull(newEntity)) {
                diffBuilder.removeListItem(field, oldEntity);
                continue;
            }

            if (Objects.isNull(oldEntity) && Objects.nonNull(newEntity)) {
                diffBuilder.addListItem(field, newEntity);
                continue;
            }
            if (!Objects.equals(oldEntity, newEntity)) {
                EntityDiff entityDiff = diffEntity(field, oldEntity, newEntity);
                diffBuilder.modifyListItem(entityDiff);
            }
        }
        return diffBuilder.buildList();
    }

    private static <T extends Entity<?>> Map<Identifier, T> buildMap(List<T> values) {
        Map<Identifier, T> map = new HashMap<>();
        if (null != values) {
            for (T item : values) {
                map.put(item.getId(), item);
            }
        }
        return map;
    }


}




