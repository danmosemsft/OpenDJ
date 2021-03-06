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
 * Copyright 2006-2009 Sun Microsystems, Inc.
 * Portions Copyright 2013-2016 ForgeRock AS.
 */
package org.opends.server.api;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.forgerock.i18n.LocalizableMessage;
import org.forgerock.opendj.server.config.server.ExtendedOperationHandlerCfg;
import org.forgerock.opendj.config.server.ConfigException;
import org.opends.server.core.DirectoryServer;
import org.opends.server.core.ExtendedOperation;
import org.opends.server.types.InitializationException;

/**
 * This class defines the set of methods and structures that must be
 * implemented by a Directory Server module that implements the
 * functionality required for one or more types of extended
 * operations.
 *
 * @param <T> The configuration class that will be provided to
 *            initialize the handler.
 */
@org.opends.server.types.PublicAPI(
     stability=org.opends.server.types.StabilityLevel.VOLATILE,
     mayInstantiate=false,
     mayExtend=true,
     mayInvoke=false)
public abstract class
     ExtendedOperationHandler<T extends ExtendedOperationHandlerCfg>
{

  /** The default set of supported control OIDs for this extended op. */
  private final Set<String> supportedControlOIDs;

  /** The default set of supported feature OIDs for this extended op. */
  private final Set<String> supportedFeatureOIDs = Collections.emptySet();

  /**
   * Builds an extended operation.
   */
  public ExtendedOperationHandler()
  {
    this.supportedControlOIDs = Collections.<String> emptySet();
  }

  /**
   * Builds an extended operation.
   *
   * @param supportedControlOIDs
   *          the default set of supported control OIDs for this extended op
   */
  public ExtendedOperationHandler(Set<String> supportedControlOIDs)
  {
    this.supportedControlOIDs = supportedControlOIDs != null ?
        Collections.unmodifiableSet(supportedControlOIDs)
        : Collections.<String> emptySet();
  }

  /**
   * Initializes this extended operation handler based on the
   * information in the provided configuration entry.  It should also
   * register itself with the Directory Server for the particular
   * kinds of extended operations that it will process.
   *
   * @param  config  The extended operation handler configuration that
   *                 contains the information to use to initialize
   *                 this extended operation handler.
   *
   * @throws  ConfigException  If an unrecoverable problem arises in
   *                           the process of performing the
   *                           initialization.
   *
   * @throws  InitializationException  If a problem occurs
   *                                   during initialization that is
   *                                   not related to the server
   *                                   configuration.
   */
  public void initializeExtendedOperationHandler(T config)
      throws ConfigException, InitializationException
  {
    DirectoryServer.registerSupportedExtension(getExtendedOperationOID(), this);
    registerControlsAndFeatures();
  }



  /**
   * Indicates whether the provided configuration is acceptable for
   * this extended operation handler.  It should be possible to call
   * this method on an uninitialized extended operation handler
   * instance in order to determine whether the extended operation
   * handler would be able to use the provided configuration.
   * <BR><BR>
   * Note that implementations which use a subclass of the provided
   * configuration class will likely need to cast the configuration
   * to the appropriate subclass type.
   *
   * @param  configuration        The extended operation handler
   *                              configuration for which to make the
   *                              determination.
   * @param  unacceptableReasons  A list that may be used to hold the
   *                              reasons that the provided
   *                              configuration is not acceptable.
   *
   * @return  {@code true} if the provided configuration is acceptable
   *          for this extended operation handler, or {@code false} if
   *          not.
   */
  public boolean isConfigurationAcceptable(
                      ExtendedOperationHandlerCfg configuration,
                      List<LocalizableMessage> unacceptableReasons)
  {
    // This default implementation does not perform any special
    // validation.  It should be overridden by extended operation
    // handler implementations that wish to perform more detailed
    // validation.
    return true;
  }



  /**
   * Performs any finalization that may be necessary for this extended
   * operation handler.  By default, no finalization is performed.
   */
  public void finalizeExtendedOperationHandler()
  {
    DirectoryServer.deregisterSupportedExtension(getExtendedOperationOID());
    deregisterControlsAndFeatures();
  }



  /**
   * Processes the provided extended operation.
   *
   * @param  operation  The extended operation to be processed.
   */
  public abstract void processExtendedOperation(ExtendedOperation
                                                     operation);



  /**
   * Retrieves the OIDs of the controls that may be supported by this
   * extended operation handler.  It should be overridden by any
   * extended operation handler which provides special support for one
   * or more controls.
   *
   * @return  The OIDs of the controls that may be supported by this
   *          extended operation handler.
   */
  public Set<String> getSupportedControls()
  {
    return supportedControlOIDs;
  }



  /**
   * Indicates whether this extended operation handler supports the
   * specified control.
   *
   * @param  controlOID  The OID of the control for which to make the
   *                     determination.
   *
   * @return  {@code true} if this extended operation handler does
   *          support the requested control, or {@code false} if not.
   */
  public final boolean supportsControl(String controlOID)
  {
    return getSupportedControls().contains(controlOID);
  }



  /**
   * Retrieves the OIDs of the features that may be supported by this
   * extended operation handler.
   *
   * @return  The OIDs of the features that may be supported by this
   *          extended operation handler.
   */
  public Set<String> getSupportedFeatures()
  {
    return supportedFeatureOIDs;
  }



  /**
   * Indicates whether this extended operation handler supports the
   * specified feature.
   *
   * @param  featureOID  The OID of the feature for which to make the
   *                     determination.
   *
   * @return  {@code true} if this extended operation handler does
   *          support the requested feature, or {@code false} if not.
   */
  public final boolean supportsFeature(String featureOID)
  {
    return getSupportedFeatures().contains(featureOID);
  }



  /**
   * If the extended operation handler defines any supported controls
   * and/or features, then register them with the server.
   */
  private void registerControlsAndFeatures()
  {
    Set<String> controlOIDs = getSupportedControls();
    if (controlOIDs != null)
    {
      for (String oid : controlOIDs)
      {
        DirectoryServer.registerSupportedControl(oid);
      }
    }

    Set<String> featureOIDs = getSupportedFeatures();
    if (featureOIDs != null)
    {
      for (String oid : featureOIDs)
      {
        DirectoryServer.registerSupportedFeature(oid);
      }
    }
  }



  /**
   * If the extended operation handler defines any supported controls
   * and/or features, then deregister them with the server.
   */
  private void deregisterControlsAndFeatures()
  {
    Set<String> controlOIDs = getSupportedControls();
    if (controlOIDs != null)
    {
      for (String oid : controlOIDs)
      {
        DirectoryServer.deregisterSupportedControl(oid);
      }
    }

    Set<String> featureOIDs = getSupportedFeatures();
    if (featureOIDs != null)
    {
      for (String oid : featureOIDs)
      {
        DirectoryServer.deregisterSupportedFeature(oid);
      }
    }
  }

  /**
   * Retrieves the name associated with this extended operation.
   *
   * @return  The name associated with this extended operation,
   *          if any, or <CODE>null</CODE> if there is none.
   */
  public String getExtendedOperationName()
  {
    return null;
  }

  /**
   * Retrieves the object OID associated with this extended operation.
   *
   * @return the oid associated with this extended operation, if any, or
   *         <CODE>null</CODE> if there is none.
   */
  public String getExtendedOperationOID()
  {
    return null;
  }
}

