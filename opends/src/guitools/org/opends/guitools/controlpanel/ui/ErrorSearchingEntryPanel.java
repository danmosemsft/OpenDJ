/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE
 * or https://OpenDS.dev.java.net/OpenDS.LICENSE.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE.  If applicable,
 * add the following below this CDDL HEADER, with the fields enclosed
 * by brackets "[]" replaced with your own identifying information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2008 Sun Microsystems, Inc.
 */

package org.opends.guitools.controlpanel.ui;

import static org.opends.messages.AdminToolMessages.*;

import java.awt.Component;
import java.awt.GridBagConstraints;

import org.opends.guitools.controlpanel.event.ConfigurationChangeEvent;
import org.opends.messages.Message;
import org.opends.server.types.OpenDsException;

/**
 * The panel that is displayed when there is an error searching an entry.
 *
 */
public class ErrorSearchingEntryPanel extends StatusGenericPanel
{
  private static final long serialVersionUID = -8460172599072631973L;

  /**
   * Default constructor.
   *
   */
  public ErrorSearchingEntryPanel()
  {
    super();
    GridBagConstraints gbc = new GridBagConstraints();
    addErrorPane(gbc);
    errorPane.setVisible(true);
  }

  /**
   * {@inheritDoc}
   */
  public Component getPreferredFocusComponent()
  {
    return errorPane;
  }

  /**
   * {@inheritDoc}
   */
  public void okClicked()
  {
  }

  /**
   * {@inheritDoc}
   */
  public Message getTitle()
  {
    return INFO_CTRL_PANEL_ERROR_SEARCHING_ENTRY_TITLE.get();
  }

  /**
   * {@inheritDoc}
   */
  public void configurationChanged(ConfigurationChangeEvent ev)
  {

  }

  /**
   * Sets the error to be displayed in the panel.
   * @param dn the DN of the entry that caused a problem.
   * @param t the Throwable that occurred when searching the entry.
   */
  public void setError(String dn, Throwable t)
  {
    Message title = INFO_CTRL_PANEL_ERROR_SEARCHING_ENTRY_TITLE.get();
    Message details;
    if (t instanceof OpenDsException)
    {
      details = ERR_CTRL_PANEL_ERROR_SEARCHING_ENTRY.get(dn,
      ((OpenDsException)t).getMessageObject().toString());
    }
    else
    {
      details = ERR_CTRL_PANEL_ERROR_SEARCHING_ENTRY.get(dn,
          t.toString());
    }
    updateErrorPane(errorPane, title, ColorAndFontConstants.errorTitleFont,
        details, ColorAndFontConstants.defaultFont);
  }
}
