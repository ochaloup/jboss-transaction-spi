/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.tm;

import javax.transaction.xa.XAResource;

/**
 * <p>
 * XAResource which indicates what is its preference for ordering
 * during two-phase commit processing.
 * <p>
 * The lower value of ordering hint means the earlier the XAResorce get to be processed.
 * The default ordering hint is <code>0</code>. When the XAResource implementing
 * this interface returns the zero then the ordering as left as it was originally.
 * The value <code>0</code> is considered for XAResources not implementing this interface.
 * <p>
 * The first are taken resources hinting with value {@link Short#MIN_VALUE}, the latest
 * are taken into processing resources hinting with value {@link Short#MAX_VALUE}.
 * <p>
 * The hinting method could be called several times on the XAResource itself. The transaction
 * manager does not ensures that it gets a hint first and then tries to get it again
 * before or after end call.
 * <p>
 * One of the reasoning for this hint is processing of two phase commit in respect for applying
 * 1PC optimization. Transaction manager orders the resources just before {@link XAResource#end(javax.transaction.xa.Xid, int)}
 * is called to process the <code>end</code> and <code>prepare</code> for each resource as one action unit
 * but in the hinted order. The resource which wants to be 1PC optimized while it knows there was no work done on it so far
 * it should report values lower than <code>0</code>.
 */
public interface XAResourceWithPriorityHint extends javax.transaction.xa.XAResource {

    /**
     * <p>
     * Hinting the order that the XAResource prioritize itself for 2PC processing
     * during <b>prepare phase</b>.
     * <p>
     * The lower value the earlier processing<br>
     * Expecting this method to be idempotent returning the same value each
     * time it's requested.
     *
     * @return  order hint, the earliest is taken one returning {@link Short#MIN_VALUE},
     *    the latest is taken one returning {@link Short#MAX_VALUE}.
     */
    short getPreparePriorityHint();
}
