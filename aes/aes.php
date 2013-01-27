<?php
class AesCrypter
{
    private $key = 'sfe023f_9fd&fwfl';
    private $encrypter;

    public function __construct($key = '', $algorithm = MCRYPT_RIJNDAEL_128, $mode = MCRYPT_MODE_CBC)
    {
        if (!empty($key)) {
            $this->key = $key;
        }
        $this->encrypter = mcrypt_module_open($algorithm, '', $mode, '');
    }

    public function encrypt($origData)
    {
        $origData = pkcs5padding($origData, mcrypt_enc_get_block_size($this->encrypter));
        mcrypt_generic_init($this->encrypter, $this->key, substr($this->key, 0, 16));
        $ciphertext = mcrypt_generic($this->encrypter, $origData);
        mcrypt_generic_deinit($this->encrypter);
        return $ciphertext;
    }

    public function decrypt($ciphertext)
    {
        mcrypt_generic_init($this->encrypter, $this->key, substr($this->key, 0, 16));
        $origData = mdecrypt_generic($this->encrypter, $ciphertext);
        mcrypt_generic_deinit($this->encrypter);
        return pkcs5unPadding($origData);
    }

    public function close()
    {
        mcrypt_module_close($this->encrypter);
    }
}

function pkcs5padding($data, $blocksize)
{
    $padding = $blocksize - strlen($data) % $blocksize;
    $paddingText = str_repeat(chr($padding), $padding);
    return $data . $paddingText;
}

function pkcs5unPadding($data)
{
    $length = strlen($data);
    $unpadding = ord($data[$length - 1]);
    return substr($data, 0, $length - $unpadding);
}

// AES-128
$encrypter = new AesCrypter();
$data = 'polaris@studygolang';
$result = $encrypter->encrypt($data);
var_dump(base64_encode($result));
var_dump(rtrim($encrypter->decrypt($result)));
$encrypter->close();
