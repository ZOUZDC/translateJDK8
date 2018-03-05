/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;
import java.util.*;

/**
 * A {@link Deque} that additionally supports blocking operations that wait
 * for the deque to become non-empty when retrieving an element, and wait for
 * space to become available in the deque when storing an element.
 *
 * 它是一个双端对列,对以下功能支持阻塞操作: 当检索元素时，在队列非空之前对该对列支持阻塞, 在存储元素时,对该对列的的可用空间提供阻塞
 *
 *
 * <p>{@code BlockingDeque} methods come in four forms, with different ways
 * of handling operations that cannot be satisfied immediately, but may be
 * satisfied at some point in the future:
 * one throws an exception, the second returns a special value (either
 * {@code null} or {@code false}, depending on the operation), the third
 * blocks the current thread indefinitely until the operation can succeed,
 * and the fourth blocks for only a given maximum time limit before giving
 * up.  These methods are summarized in the following table:
 *
 * <p>阻塞队列（BlockinDeque)方法 有四种形式,使用不同的方式处理无法立刻满足但在将来的某一刻可能满足的操作:
 * 一抛异常 ,两返回一个特殊的值(null或者 false ,依据操作), 三 阻塞当前线程直到操作成功  四 阻塞当前线程当达到指定的最大值的时候放弃
 * 这些方法总结在下表中：
 *
 *
 * <table BORDER CELLPADDING=3 CELLSPACING=1>
 * <caption>Summary of BlockingDeque methods</caption>
 *  <tr>
 *    <td ALIGN=CENTER COLSPAN = 5> <b>First Element (Head)</b></td>
 *  </tr>
 *  <tr>
 *    <td></td>
 *    <td ALIGN=CENTER><em>Throws exception</em></td>
 *    <td ALIGN=CENTER><em>Special value</em></td>
 *    <td ALIGN=CENTER><em>Blocks</em></td>
 *    <td ALIGN=CENTER><em>Times out</em></td>
 *  </tr>
 *  <tr>
 *    <td><b>Insert</b></td>
 *    <td>{@link #addFirst addFirst(e)}</td>
 *    <td>{@link #offerFirst(Object) offerFirst(e)}</td>
 *    <td>{@link #putFirst putFirst(e)}</td>
 *    <td>{@link #offerFirst(Object, long, TimeUnit) offerFirst(e, time, unit)}</td>
 *  </tr>
 *  <tr>
 *    <td><b>Remove</b></td>
 *    <td>{@link #removeFirst removeFirst()}</td>
 *    <td>{@link #pollFirst pollFirst()}</td>
 *    <td>{@link #takeFirst takeFirst()}</td>
 *    <td>{@link #pollFirst(long, TimeUnit) pollFirst(time, unit)}</td>
 *  </tr>
 *  <tr>
 *    <td><b>Examine</b></td>
 *    <td>{@link #getFirst getFirst()}</td>
 *    <td>{@link #peekFirst peekFirst()}</td>
 *    <td><em>not applicable</em></td>
 *    <td><em>not applicable</em></td>
 *  </tr>
 *  <tr>
 *    <td ALIGN=CENTER COLSPAN = 5> <b>Last Element (Tail)</b></td>
 *  </tr>
 *  <tr>
 *    <td></td>
 *    <td ALIGN=CENTER><em>Throws exception</em></td>
 *    <td ALIGN=CENTER><em>Special value</em></td>
 *    <td ALIGN=CENTER><em>Blocks</em></td>
 *    <td ALIGN=CENTER><em>Times out</em></td>
 *  </tr>
 *  <tr>
 *    <td><b>Insert</b></td>
 *    <td>{@link #addLast addLast(e)}</td>
 *    <td>{@link #offerLast(Object) offerLast(e)}</td>
 *    <td>{@link #putLast putLast(e)}</td>
 *    <td>{@link #offerLast(Object, long, TimeUnit) offerLast(e, time, unit)}</td>
 *  </tr>
 *  <tr>
 *    <td><b>Remove</b></td>
 *    <td>{@link #removeLast() removeLast()}</td>
 *    <td>{@link #pollLast() pollLast()}</td>
 *    <td>{@link #takeLast takeLast()}</td>
 *    <td>{@link #pollLast(long, TimeUnit) pollLast(time, unit)}</td>
 *  </tr>
 *  <tr>
 *    <td><b>Examine</b></td>
 *    <td>{@link #getLast getLast()}</td>
 *    <td>{@link #peekLast peekLast()}</td>
 *    <td><em>not applicable</em></td>
 *    <td><em>not applicable</em></td>
 *  </tr>
 * </table>
 *
 * <p>Like any {@link BlockingQueue}, a {@code BlockingDeque} is thread safe,
 * does not permit null elements, and may (or may not) be
 * capacity-constrained.
 *
 * <p>像BlockingQueue一样，BlockingDeque是线程安全的,但是不先允许null元素, 可能(或不可能)容量受限
 *
 *
 * <p>A {@code BlockingDeque} implementation may be used directly as a FIFO
 * {@code BlockingQueue}. The methods inherited from the
 * {@code BlockingQueue} interface are precisely equivalent to
 * {@code BlockingQueue} methods as  in the following table:
 *
 * <p>BlockingQueue实现并可以直接用作 FIFO(First Input First Output) BlockingQueue . 这个方法继承了BlockingQueue接口 等价于BlockQueue方法
 * 如下图所示:
 *
 *
 * <table BORDER CELLPADDING=3 CELLSPACING=1>
 * <caption>Comparison of BlockingQueue and BlockingDeque methods</caption>
 *  <tr>
 *    <td ALIGN=CENTER> <b>{@code BlockingQueue} Method</b></td>
 *    <td ALIGN=CENTER> <b>Equivalent {@code BlockingDeque} Method</b></td>
 *  </tr>
 *  <tr>
 *    <td ALIGN=CENTER COLSPAN = 2> <b>Insert</b></td>
 *  </tr>
 *  <tr>
 *    <td>{@link #add(Object) add(e)}</td>
 *    <td>{@link #addLast(Object) addLast(e)}</td>
 *  </tr>
 *  <tr>
 *    <td>{@link #offer(Object) offer(e)}</td>
 *    <td>{@link #offerLast(Object) offerLast(e)}</td>
 *  </tr>
 *  <tr>
 *    <td>{@link #put(Object) put(e)}</td>
 *    <td>{@link #putLast(Object) putLast(e)}</td>
 *  </tr>
 *  <tr>
 *    <td>{@link #offer(Object, long, TimeUnit) offer(e, time, unit)}</td>
 *    <td>{@link #offerLast(Object, long, TimeUnit) offerLast(e, time, unit)}</td>
 *  </tr>
 *  <tr>
 *    <td ALIGN=CENTER COLSPAN = 2> <b>Remove</b></td>
 *  </tr>
 *  <tr>
 *    <td>{@link #remove() remove()}</td>
 *    <td>{@link #removeFirst() removeFirst()}</td>
 *  </tr>
 *  <tr>
 *    <td>{@link #poll() poll()}</td>
 *    <td>{@link #pollFirst() pollFirst()}</td>
 *  </tr>
 *  <tr>
 *    <td>{@link #take() take()}</td>
 *    <td>{@link #takeFirst() takeFirst()}</td>
 *  </tr>
 *  <tr>
 *    <td>{@link #poll(long, TimeUnit) poll(time, unit)}</td>
 *    <td>{@link #pollFirst(long, TimeUnit) pollFirst(time, unit)}</td>
 *  </tr>
 *  <tr>
 *    <td ALIGN=CENTER COLSPAN = 2> <b>Examine</b></td>
 *  </tr>
 *  <tr>
 *    <td>{@link #element() element()}</td>
 *    <td>{@link #getFirst() getFirst()}</td>
 *  </tr>
 *  <tr>
 *    <td>{@link #peek() peek()}</td>
 *    <td>{@link #peekFirst() peekFirst()}</td>
 *  </tr>
 * </table>
 *
 * <p>Memory consistency effects: As with other concurrent
 * collections, actions in a thread prior to placing an object into a
 * {@code BlockingDeque}
 * <a href="package-summary.html#MemoryVisibility"><i>happen-before</i></a>
 * actions subsequent to the access or removal of that element from
 * the {@code BlockingDeque} in another thread.
 *
 * <p>内存一致性效果: 当其他并发collections,将对象放入BlockingDeque之前的线程中的操作happen-before
 * 随后通过另一个线程从BlockDeque中移除该元素的操作
 *
 *
 * <p>This interface is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @since 1.6
 * @author Doug Lea
 * @param <E> the type of elements held in this collection      <E>此集合中保存的元素类型
 *
 */
public interface BlockingDeque<E> extends BlockingQueue<E>, Deque<E> {
    /*
     * We have "diamond" multiple interface inheritance here, and that
     * introduces ambiguities.  Methods might end up with different
     * specs depending on the branch chosen by javadoc.  Thus a lot of
     * methods specs here are copied from superinterfaces.
     */

    /**
     * Inserts the specified element at the front of this deque if it is
     * possible to do so immediately without violating capacity restrictions,
     * throwing an {@code IllegalStateException} if no space is currently
     * available.  When using a capacity-restricted deque, it is generally
     * preferable to use {@link #offerFirst(Object) offerFirst}.
     *
     * <p> 在这个Deque的前面插入指定的元素,在不违反容量限制的情况下能够立刻操作,
     * 如过当前空间不可用则会抛出IllegalStateException,
     * 当使用一个容量存在限制的Deque时,通常情况下最好使用offerFirst
     *
     *
     * @param e the element to add
     * @throws IllegalStateException {@inheritDoc}
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    void addFirst(E e);

    /**
     * <p>Inserts the specified element at the end of this deque if it is
     * possible to do so immediately without violating capacity restrictions,
     * throwing an {@code IllegalStateException} if no space is currently
     * available.  When using a capacity-restricted deque, it is generally
     * preferable to use {@link #offerLast(Object) offerLast}.
     *
     * <p>在这个Deque的后面插入指定的元素,在不违反容量限制的情况下能够立刻操作,
     * 如过当前空间不可用则会抛出IllegalStateException,
     * 当使用一个容量存在限制的Deque时,通常情况下最好使用offerFirst
     *
     *
     * @param e the element to add
     * @throws IllegalStateException {@inheritDoc}
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    void addLast(E e);

    /**
     * Inserts the specified element at the front of this deque if it is
     * possible to do so immediately without violating capacity restrictions,
     * returning {@code true} upon success and {@code false} if no space is
     * currently available.
     * When using a capacity-restricted deque, this method is generally
     * preferable to the {@link #addFirst(Object) addFirst} method, which can
     * fail to insert an element only by throwing an exception.
     *
     * <p>在这个Deque的前面插入指定的元素,在不违反容量限制的情况下能够立刻操作,当成功能的时候返回true,当前当前没有可用的空间时返回false.
     * 当使用一个容量存在限制的Deque时,通常情况下这个方法优于addFirst方法,因为插入失败的时候能够抛异常
     *
     *
     * @param e the element to add
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    boolean offerFirst(E e);

    /**
     * Inserts the specified element at the end of this deque if it is
     * possible to do so immediately without violating capacity restrictions,
     * returning {@code true} upon success and {@code false} if no space is
     * currently available.
     * When using a capacity-restricted deque, this method is generally
     * preferable to the {@link #addLast(Object) addLast} method, which can
     * fail to insert an element only by throwing an exception.
     *
     * <p>在这个Deque的后面插入指定的元素,在不违反容量限制的情况下能够立刻操作,当成功能的时候返回true,当前当前没有可用的空间时返回false
     * 当使用一个容量存在限制的Deque时,通常情况下这个方法优于addLast方法,因为插入失败的时候能够抛异常
     *
     *
     * @param e the element to add
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    boolean offerLast(E e);

    /**
     * Inserts the specified element at the front of this deque,
     * waiting if necessary for space to become available.
     *
     * @param e the element to add
     * @throws InterruptedException if interrupted while waiting
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this deque
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */
    void putFirst(E e) throws InterruptedException;

    /**
     * Inserts the specified element at the end of this deque,
     * waiting if necessary for space to become available.
     *
     * @param e the element to add
     * @throws InterruptedException if interrupted while waiting
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this deque
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */
    void putLast(E e) throws InterruptedException;

    /**
     * Inserts the specified element at the front of this deque,
     * waiting up to the specified wait time if necessary for space to
     * become available.
     *
     * @param e the element to add
     * @param timeout how long to wait before giving up, in units of
     *        {@code unit}
     * @param unit a {@code TimeUnit} determining how to interpret the
     *        {@code timeout} parameter
     * @return {@code true} if successful, or {@code false} if
     *         the specified waiting time elapses before space is available
     * @throws InterruptedException if interrupted while waiting
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this deque
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */
    boolean offerFirst(E e, long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * Inserts the specified element at the end of this deque,
     * waiting up to the specified wait time if necessary for space to
     * become available.
     *
     * @param e the element to add
     * @param timeout how long to wait before giving up, in units of
     *        {@code unit}
     * @param unit a {@code TimeUnit} determining how to interpret the
     *        {@code timeout} parameter
     * @return {@code true} if successful, or {@code false} if
     *         the specified waiting time elapses before space is available
     * @throws InterruptedException if interrupted while waiting
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this deque
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */
    boolean offerLast(E e, long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * Retrieves and removes the first element of this deque, waiting
     * if necessary until an element becomes available.
     *
     * @return the head of this deque
     * @throws InterruptedException if interrupted while waiting
     */
    E takeFirst() throws InterruptedException;

    /**
     * Retrieves and removes the last element of this deque, waiting
     * if necessary until an element becomes available.
     *
     * @return the tail of this deque
     * @throws InterruptedException if interrupted while waiting
     */
    E takeLast() throws InterruptedException;

    /**
     * Retrieves and removes the first element of this deque, waiting
     * up to the specified wait time if necessary for an element to
     * become available.
     *
     * @param timeout how long to wait before giving up, in units of
     *        {@code unit}
     * @param unit a {@code TimeUnit} determining how to interpret the
     *        {@code timeout} parameter
     * @return the head of this deque, or {@code null} if the specified
     *         waiting time elapses before an element is available
     * @throws InterruptedException if interrupted while waiting
     */
    E pollFirst(long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * Retrieves and removes the last element of this deque, waiting
     * up to the specified wait time if necessary for an element to
     * become available.
     *
     * @param timeout how long to wait before giving up, in units of
     *        {@code unit}
     * @param unit a {@code TimeUnit} determining how to interpret the
     *        {@code timeout} parameter
     * @return the tail of this deque, or {@code null} if the specified
     *         waiting time elapses before an element is available
     * @throws InterruptedException if interrupted while waiting
     */
    E pollLast(long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * Removes the first occurrence of the specified element from this deque.
     * If the deque does not contain the element, it is unchanged.
     * More formally, removes the first element {@code e} such that
     * {@code o.equals(e)} (if such an element exists).
     * Returns {@code true} if this deque contained the specified element
     * (or equivalently, if this deque changed as a result of the call).
     *
     * @param o element to be removed from this deque, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     */
    boolean removeFirstOccurrence(Object o);

    /**
     * Removes the last occurrence of the specified element from this deque.
     * If the deque does not contain the element, it is unchanged.
     * More formally, removes the last element {@code e} such that
     * {@code o.equals(e)} (if such an element exists).
     * Returns {@code true} if this deque contained the specified element
     * (or equivalently, if this deque changed as a result of the call).
     *
     * @param o element to be removed from this deque, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     */
    boolean removeLastOccurrence(Object o);

    // *** BlockingQueue methods ***

    /**
     * Inserts the specified element into the queue represented by this deque
     * (in other words, at the tail of this deque) if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * {@code true} upon success and throwing an
     * {@code IllegalStateException} if no space is currently available.
     * When using a capacity-restricted deque, it is generally preferable to
     * use {@link #offer(Object) offer}.
     *
     * <p>This method is equivalent to {@link #addLast(Object) addLast}.
     *
     * @param e the element to add
     * @throws IllegalStateException {@inheritDoc}
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this deque
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */
    boolean add(E e);

    /**
     * Inserts the specified element into the queue represented by this deque
     * (in other words, at the tail of this deque) if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * {@code true} upon success and {@code false} if no space is currently
     * available.  When using a capacity-restricted deque, this method is
     * generally preferable to the {@link #add} method, which can fail to
     * insert an element only by throwing an exception.
     *
     * <p>This method is equivalent to {@link #offerLast(Object) offerLast}.
     *
     * @param e the element to add
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this deque
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */
    boolean offer(E e);

    /**
     * Inserts the specified element into the queue represented by this deque
     * (in other words, at the tail of this deque), waiting if necessary for
     * space to become available.
     *
     * <p>This method is equivalent to {@link #putLast(Object) putLast}.
     *
     * @param e the element to add
     * @throws InterruptedException {@inheritDoc}
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this deque
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */
    void put(E e) throws InterruptedException;

    /**
     * Inserts the specified element into the queue represented by this deque
     * (in other words, at the tail of this deque), waiting up to the
     * specified wait time if necessary for space to become available.
     *
     * <p>This method is equivalent to
     * {@link #offerLast(Object,long,TimeUnit) offerLast}.
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this deque, else
     *         {@code false}
     * @throws InterruptedException {@inheritDoc}
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this deque
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */
    boolean offer(E e, long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * Retrieves and removes the head of the queue represented by this deque
     * (in other words, the first element of this deque).
     * This method differs from {@link #poll poll} only in that it
     * throws an exception if this deque is empty.
     *
     * <p>This method is equivalent to {@link #removeFirst() removeFirst}.
     *
     * @return the head of the queue represented by this deque
     * @throws NoSuchElementException if this deque is empty
     */
    E remove();

    /**
     * Retrieves and removes the head of the queue represented by this deque
     * (in other words, the first element of this deque), or returns
     * {@code null} if this deque is empty.
     *
     * <p>This method is equivalent to {@link #pollFirst()}.
     *
     * @return the head of this deque, or {@code null} if this deque is empty
     */
    E poll();

    /**
     * Retrieves and removes the head of the queue represented by this deque
     * (in other words, the first element of this deque), waiting if
     * necessary until an element becomes available.
     *
     * <p>This method is equivalent to {@link #takeFirst() takeFirst}.
     *
     * @return the head of this deque
     * @throws InterruptedException if interrupted while waiting
     */
    E take() throws InterruptedException;

    /**
     * Retrieves and removes the head of the queue represented by this deque
     * (in other words, the first element of this deque), waiting up to the
     * specified wait time if necessary for an element to become available.
     *
     * <p>This method is equivalent to
     * {@link #pollFirst(long,TimeUnit) pollFirst}.
     *
     * @return the head of this deque, or {@code null} if the
     *         specified waiting time elapses before an element is available
     * @throws InterruptedException if interrupted while waiting
     */
    E poll(long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * Retrieves, but does not remove, the head of the queue represented by
     * this deque (in other words, the first element of this deque).
     * This method differs from {@link #peek peek} only in that it throws an
     * exception if this deque is empty.
     *
     * <p>This method is equivalent to {@link #getFirst() getFirst}.
     *
     * @return the head of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    E element();

    /**
     * Retrieves, but does not remove, the head of the queue represented by
     * this deque (in other words, the first element of this deque), or
     * returns {@code null} if this deque is empty.
     *
     * <p>This method is equivalent to {@link #peekFirst() peekFirst}.
     *
     * @return the head of this deque, or {@code null} if this deque is empty
     */
    E peek();

    /**
     * Removes the first occurrence of the specified element from this deque.
     * If the deque does not contain the element, it is unchanged.
     * More formally, removes the first element {@code e} such that
     * {@code o.equals(e)} (if such an element exists).
     * Returns {@code true} if this deque contained the specified element
     * (or equivalently, if this deque changed as a result of the call).
     *
     * <p>This method is equivalent to
     * {@link #removeFirstOccurrence(Object) removeFirstOccurrence}.
     *
     * @param o element to be removed from this deque, if present
     * @return {@code true} if this deque changed as a result of the call
     * @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     */
    boolean remove(Object o);

    /**
     * Returns {@code true} if this deque contains the specified element.
     * More formally, returns {@code true} if and only if this deque contains
     * at least one element {@code e} such that {@code o.equals(e)}.
     *
     * @param o object to be checked for containment in this deque
     * @return {@code true} if this deque contains the specified element
     * @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     */
    public boolean contains(Object o);

    /**
     * Returns the number of elements in this deque.
     *
     * @return the number of elements in this deque
     */
    public int size();

    /**
     * Returns an iterator over the elements in this deque in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this deque in proper sequence
     */
    Iterator<E> iterator();

    // *** Stack methods ***

    /**
     * Pushes an element onto the stack represented by this deque (in other
     * words, at the head of this deque) if it is possible to do so
     * immediately without violating capacity restrictions, throwing an
     * {@code IllegalStateException} if no space is currently available.
     *
     * <p>This method is equivalent to {@link #addFirst(Object) addFirst}.
     *
     * @throws IllegalStateException {@inheritDoc}
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    void push(E e);
}
