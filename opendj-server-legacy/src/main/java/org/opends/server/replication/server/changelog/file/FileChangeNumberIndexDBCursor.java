/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyright [year] [name of copyright owner]".
 *
 * Copyright 2014-2016 ForgeRock AS.
 */
package org.opends.server.replication.server.changelog.file;

import net.jcip.annotations.NotThreadSafe;

import org.opends.server.replication.server.changelog.api.ChangeNumberIndexRecord;
import org.opends.server.replication.server.changelog.api.ChangelogException;
import org.opends.server.replication.server.changelog.api.DBCursor;

/** A cursor on ChangeNumberIndexDB. */
@NotThreadSafe
class FileChangeNumberIndexDBCursor implements DBCursor<ChangeNumberIndexRecord>
{
  /** The underlying cursor. */
  private final DBCursor<Record<Long, ChangeNumberIndexRecord>> cursor;

  /**
   * Creates the cursor from provided cursor.
   *
   * @param cursor
   *          The underlying cursor to read log.
   * @throws ChangelogException
   *            If an error occurs.
   */
  FileChangeNumberIndexDBCursor(final DBCursor<Record<Long, ChangeNumberIndexRecord>> cursor)
      throws ChangelogException
  {
    this.cursor = cursor;
  }

  /** {@inheritDoc} */
  @Override
  public ChangeNumberIndexRecord getRecord()
  {
    final Record<Long, ChangeNumberIndexRecord> record = cursor.getRecord();
    return record != null ? record.getValue() : null;
  }

  /** {@inheritDoc} */
  @Override
  public boolean next() throws ChangelogException
  {
    return cursor.next();
  }

  /** {@inheritDoc} */
  @Override
  public void close()
  {
    cursor.close();
  }

}
