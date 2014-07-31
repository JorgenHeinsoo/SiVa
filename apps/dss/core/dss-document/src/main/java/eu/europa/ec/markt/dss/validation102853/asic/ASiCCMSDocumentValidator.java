/*
 * DSS - Digital Signature Services
 *
 * Copyright (C) 2013 European Commission, Directorate-General Internal Market and Services (DG MARKT), B-1049 Bruxelles/Brussel
 *
 * Developed by: 2013 ARHS Developments S.A. (rue Nicolas Bové 2B, L-1253 Luxembourg) http://www.arhs-developments.com
 *
 * This file is part of the "DSS - Digital Signature Services" project.
 *
 * "DSS - Digital Signature Services" is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * DSS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * "DSS - Digital Signature Services".  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.markt.dss.validation102853.asic;

import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.MimeType;
import eu.europa.ec.markt.dss.validation102853.cades.CMSDocumentValidator;

/**
 * Validator for ASiC signature
 * <p/>
 * <p/>
 * DISCLAIMER: Project owner DG-MARKT.
 *
 * @author <a href="mailto:dgmarkt.Project-DSS@arhs-developments.com">ARHS Developments</a>
 * @version $Revision: 672 $ - $Date: 2011-05-12 11:59:21 +0200 (Thu, 12 May 2011) $
 */
public class ASiCCMSDocumentValidator extends CMSDocumentValidator {

	/**
	 * This mime-type comes from the container file name: (zip, asic...).
	 */
	private MimeType asicContainerMimeType;

	/**
	 * This mime-type comes from the 'mimetype' file included within the container.
	 */
	private MimeType asicMimeType;

	/**
	 * This mime-type comes from the ZIP comment:<br/>
	 * The comment field in the ZIP header may be used to identify the type of the data object within the container.
	 * If this field is present, it should be set with "mimetype=" followed by the mime type of the data object held in
	 * the signed data object.
	 */
	protected MimeType asicCommentMimeType;

	/**
	 * This mime-type comes from the "magic number".
	 */
	private MimeType magicNumberMimeType;

	/**
	 * The default constructor for ASiCXMLDocumentValidator.
	 *
	 * @param signature
	 * @param signedContent
	 * @param signedDocumentFileName
	 * @throws DSSException
	 */
	public ASiCCMSDocumentValidator(final byte[] signature, final byte[] signedContent, final String signedDocumentFileName) throws DSSException {

		super(new InMemoryDocument(signature));
		detachedContent = new InMemoryDocument(signedContent, signedDocumentFileName);
	}

	public MimeType getAsicContainerMimeType() {
		return asicContainerMimeType;
	}

	public void setAsicContainerMimeType(final MimeType asicContainerMimeType) {
		this.asicContainerMimeType = asicContainerMimeType;
	}

	public MimeType getAsicMimeType() {
		return asicMimeType;
	}

	public void setAsicMimeType(final MimeType asicMimeType) {
		this.asicMimeType = asicMimeType;
	}

	public MimeType getAsicCommentMimeType() {
		return asicCommentMimeType;
	}

	public void setAsicCommentMimeType(final MimeType asicCommentMimeType) {
		this.asicCommentMimeType = asicCommentMimeType;
	}

	public MimeType getMagicNumberMimeType() {
		return magicNumberMimeType;
	}

	public void setMagicNumberMimeType(final MimeType magicNumberMimeType) {
		this.magicNumberMimeType = magicNumberMimeType;
	}
}