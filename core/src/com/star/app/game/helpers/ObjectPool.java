package com.star.app.game.helpers;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectPool<T extends Poolable> {
    // Список активных элементов
    protected List<T> activeList;
    // Список свободных элементов
    protected List<T> freeList;

    public ObjectPool() {
        this.activeList = new ArrayList<T>();
        this.freeList = new ArrayList<T>();
    }

    public List<T> getActiveList() {
        return activeList;
    }

    // Объясняем пулу объектов как и какой объект надо создавать
    protected abstract T newObject();

    // Освобождаем элемент, который отработал свое. Перекидываем его из активных в свободные
    public void free(int index) {
        freeList.add(activeList.remove(index));
    }

    // Запрашиваем элемент для работыю Если пул свободных элементов пуст, то в нем
    // создается новый объект. Получаем крайний элемент из свобобдных, убираем его из
    // списка свободный элементов, перекладываем в список активных и возвращаем
    // пользователю ссылку на только что полученный активный элемент чтобы его
    // можно было настроить
    public T getActiveElement() {
        if (freeList.size() == 0) {
            freeList.add(newObject());
        }
        T temp = freeList.remove(freeList.size() - 1);
        activeList.add(temp);
        return temp;
    }

    // Проверяем список активных элементов на наличие "отработавших свое". Если
    // такие находятся, то возвращаем их в список свободных элементов.
    public void checkPool() {
        for (int i = activeList.size() - 1; i >= 0; i--) {
            if (!activeList.get(i).isActive()) {
                free(i);
            }
        }
    }
}
